package org.iop.ns.chat.structure;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOperator;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOrder;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilter;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilterGroup;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.iop.ns.chat.structure.ChatNetworkServiceDataBaseConstants.CONTACT_ID_COLUMN_NAME;
import static org.iop.ns.chat.structure.ChatNetworkServiceDataBaseConstants.CONTACT_NAME_COLUMN_NAME;
import static org.iop.ns.chat.structure.ChatNetworkServiceDataBaseConstants.CONTACT_TABLE;
import static org.iop.ns.chat.structure.ChatNetworkServiceDataBaseConstants.MESSAGES_DATE_COLUMN_NAME;
import static org.iop.ns.chat.structure.ChatNetworkServiceDataBaseConstants.MESSAGES_ID_COLUMN_NAME;
import static org.iop.ns.chat.structure.ChatNetworkServiceDataBaseConstants.MESSAGES_LOCAL_PM_COLUMN_NAME;
import static org.iop.ns.chat.structure.ChatNetworkServiceDataBaseConstants.MESSAGES_MESSAGE_COLUMN_NAME;
import static org.iop.ns.chat.structure.ChatNetworkServiceDataBaseConstants.MESSAGES_REMOTE_PK_COLUMN_NAME;
import static org.iop.ns.chat.structure.ChatNetworkServiceDataBaseConstants.MESSAGES_TABLE;

public class ContactsDAO {

    private final Database database;

    public ContactsDAO(final Database database) {

        this.database = database;
    }

    private DatabaseTable getDatabaseTable() {

        return database.getTable(CONTACT_TABLE);
    }

    /**
     * This method persists a NetworkServiceMessage in database
     *
     */
    public void persistContact(final ActorProfile actorProfile) throws Exception {

        DatabaseTable databaseTable = getDatabaseTable();

        DatabaseTableRecord databaseTableRecord = databaseTable.getEmptyRecord();

        buildDatabaseRecord(actorProfile, databaseTableRecord);

        databaseTable.insertRecord(databaseTableRecord);
    }

    /**
     * This method fills a database table record with the given data
     *
     * @param databaseTableRecord   database table record in where we put it.
     *
     * @return a DatabaseTableRecord instance.
     */
    private DatabaseTableRecord buildDatabaseRecord(final ActorProfile actorProfile,
                                                    final DatabaseTableRecord   databaseTableRecord  ) {

        databaseTableRecord.setStringValue(CONTACT_ID_COLUMN_NAME, actorProfile.getIdentityPublicKey());
        databaseTableRecord.setStringValue(CONTACT_NAME_COLUMN_NAME, actorProfile.getName());

        return databaseTableRecord;
    }

    /**
     * This method returns a NetworkServiceMessage from DatabaseTableRecord
     *
     * @param databaseTableRecord with the needed information to build the message instance.
     *
     * @return a ActorProfile instance.
     */
    private ActorProfile buildNetworkServiceMessage(DatabaseTableRecord databaseTableRecord){

        ActorProfile actorProfile = new ActorProfile();
        actorProfile.setIdentityPublicKey(databaseTableRecord.getStringValue(CONTACT_ID_COLUMN_NAME));
        actorProfile.setName(databaseTableRecord.getStringValue(CONTACT_NAME_COLUMN_NAME));
        return actorProfile;
    }


    public List<ActorProfile> listContacts() throws Exception {

        DatabaseTable table = getDatabaseTable();

        table.loadToMemory();

        List<DatabaseTableRecord> records = table.getRecords();

        List<ActorProfile> contacts = new ArrayList<>();

        for (DatabaseTableRecord record : records)
            contacts.add(buildNetworkServiceMessage(record));

        return contacts;
    }


}
