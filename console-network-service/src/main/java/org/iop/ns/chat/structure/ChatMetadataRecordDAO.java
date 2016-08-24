package org.iop.ns.chat.structure;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOperator;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOrder;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilter;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilterGroup;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.iop.ns.chat.structure.ChatNetworkServiceDataBaseConstants.MESSAGES_DATE_COLUMN_NAME;
import static org.iop.ns.chat.structure.ChatNetworkServiceDataBaseConstants.MESSAGES_ID_COLUMN_NAME;
import static org.iop.ns.chat.structure.ChatNetworkServiceDataBaseConstants.MESSAGES_LOCAL_PM_COLUMN_NAME;
import static org.iop.ns.chat.structure.ChatNetworkServiceDataBaseConstants.MESSAGES_MESSAGE_COLUMN_NAME;
import static org.iop.ns.chat.structure.ChatNetworkServiceDataBaseConstants.MESSAGES_REMOTE_PK_COLUMN_NAME;
import static org.iop.ns.chat.structure.ChatNetworkServiceDataBaseConstants.MESSAGES_TABLE;

public class ChatMetadataRecordDAO {

    private final Database database;

    public ChatMetadataRecordDAO(final Database database) {

        this.database = database;
    }

    private DatabaseTable getDatabaseTable() {

        return database.getTable(MESSAGES_TABLE);
    }

    /**
     * This method persists a NetworkServiceMessage in database
     *
     * @param networkServiceMessage instance to persist
     */
    public void persistMessage(final ChatMetadataRecord networkServiceMessage) throws Exception {

        DatabaseTable databaseTable = getDatabaseTable();

        DatabaseTableRecord databaseTableRecord = databaseTable.getEmptyRecord();

        buildDatabaseRecord(networkServiceMessage, databaseTableRecord);

        databaseTable.insertRecord(databaseTableRecord);
    }

    /**
     * This method fills a database table record with the given data
     *
     * @param networkServiceMessage with the message information
     * @param databaseTableRecord   database table record in where we put it.
     *
     * @return a DatabaseTableRecord instance.
     */
    private DatabaseTableRecord buildDatabaseRecord(final ChatMetadataRecord networkServiceMessage,
                                                    final DatabaseTableRecord   databaseTableRecord  ) {

        databaseTableRecord.setUUIDValue(MESSAGES_ID_COLUMN_NAME, networkServiceMessage.getId());
        databaseTableRecord.setStringValue(MESSAGES_LOCAL_PM_COLUMN_NAME, networkServiceMessage.getLocalActorPublicKey());
        databaseTableRecord.setStringValue(MESSAGES_REMOTE_PK_COLUMN_NAME, networkServiceMessage.getRemoteActorPublicKey());
        databaseTableRecord.setLongValue(MESSAGES_DATE_COLUMN_NAME, getLongValueFromTimestamp(networkServiceMessage.getDate()));
        databaseTableRecord.setStringValue(MESSAGES_MESSAGE_COLUMN_NAME, networkServiceMessage.getMessage());

        return databaseTableRecord;
    }

    /**
     * This method returns a NetworkServiceMessage from DatabaseTableRecord
     *
     * @param databaseTableRecord with the needed information to build the message instance.
     *
     * @return a NetworkServiceMessage instance.
     */
    private ChatMetadataRecord buildNetworkServiceMessage(DatabaseTableRecord databaseTableRecord){

        return new ChatMetadataRecord(
                databaseTableRecord.getUUIDValue(MESSAGES_ID_COLUMN_NAME),
                databaseTableRecord.getStringValue(MESSAGES_LOCAL_PM_COLUMN_NAME),
                databaseTableRecord.getStringValue(MESSAGES_REMOTE_PK_COLUMN_NAME),
                getTimestampFromLongValue(databaseTableRecord.getLongValue(MESSAGES_DATE_COLUMN_NAME)),
                databaseTableRecord.getStringValue(MESSAGES_MESSAGE_COLUMN_NAME)
        );
    }

    public List<ChatMetadataRecord> listMessages(String sender, String receiver, final Integer max, final Integer offset) throws Exception {

        DatabaseTable table = getDatabaseTable();

        if (max != null)
            table.setFilterTop(max.toString());

        if (offset != null)
            table.setFilterOffSet(offset.toString());

        List<DatabaseTableFilterGroup> filterGroups = new ArrayList<>();
        filterGroups.add(
                table.getNewFilterGroup(
                        buildFilters(table, sender, receiver), null, DatabaseFilterOperator.AND
                )
        );
        filterGroups.add(
                table.getNewFilterGroup(
                        buildFilters(table, receiver, sender), null, DatabaseFilterOperator.AND
                )
        );

        table.setFilterGroup(
                null,
                filterGroups,
                DatabaseFilterOperator.OR
        );

        table.addFilterOrder(MESSAGES_DATE_COLUMN_NAME, DatabaseFilterOrder.ASCENDING);

        table.loadToMemory();

        List<DatabaseTableRecord> records = table.getRecords();

        List<ChatMetadataRecord> networkServiceMessages = new ArrayList<>();

        for (DatabaseTableRecord record : records)
            networkServiceMessages.add(buildNetworkServiceMessage(record));

        return networkServiceMessages;
    }

    public List<ChatMetadataRecord> listMessages(final Integer max, final Integer offset) throws Exception {

        DatabaseTable table = getDatabaseTable();

        if (max != null)
            table.setFilterTop(max.toString());

        if (offset != null)
            table.setFilterOffSet(offset.toString());

        table.addFilterOrder(MESSAGES_DATE_COLUMN_NAME, DatabaseFilterOrder.ASCENDING);

        table.loadToMemory();

        List<DatabaseTableRecord> records = table.getRecords();

        List<ChatMetadataRecord> networkServiceMessages = new ArrayList<>();

        for (DatabaseTableRecord record : records)
            networkServiceMessages.add(buildNetworkServiceMessage(record));

        return networkServiceMessages;
    }

    private List<DatabaseTableFilter> buildFilters(DatabaseTable table, String sender, String receiver) {

        List<DatabaseTableFilter> filters = new ArrayList<>();

        filters.add(
                table.getNewFilter(MESSAGES_LOCAL_PM_COLUMN_NAME, DatabaseFilterType.EQUAL, sender)
        );

        filters.add(
                table.getNewFilter(MESSAGES_REMOTE_PK_COLUMN_NAME, DatabaseFilterType.EQUAL, receiver)
        );

        return filters;

    }

    /**
     * Get the timestamp representation if the value are not null
     *
     * @param value a long instance to convert
     *
     * @return a Timestamp instance or a null value
     */
    public Timestamp getTimestampFromLongValue(final Long value){

        if (value != null && value != 0){
            return new Timestamp(value);
        }else {
            return null;
        }
    }

    /**
     * Get the long value of the timestamp if are not null
     *
     * @param timestamp instance to convert
     *
     * @return a Long instance
     */
    public Long getLongValueFromTimestamp(final Timestamp timestamp){

        if (timestamp != null){
            return timestamp.getTime();
        }else {
            return Long.valueOf(0);
        }
    }

}
