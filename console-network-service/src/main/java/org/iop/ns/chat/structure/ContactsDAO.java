package org.iop.ns.chat.structure;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOperator;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOrder;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilter;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilterGroup;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginBinaryFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantLoadFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.FileNotFoundException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    private final String CONTACT_IMAGES_DIR = "images";

    private final Database database;
    private final PluginFileSystem pluginFileSystem;
    private final UUID pluginId;

    public ContactsDAO(final Database database,PluginFileSystem pluginFileSystem,UUID pluginID) {

        this.database = database;
        this.pluginFileSystem = pluginFileSystem;
        this.pluginId = pluginID;
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

        if (actorProfile.getPhoto()!=null) {
            PluginBinaryFile pluginBinaryFile = pluginFileSystem.getBinaryFile(pluginId, CONTACT_IMAGES_DIR, actorProfile.getIdentityPublicKey(), FilePrivacy.PUBLIC, FileLifeSpan.PERMANENT);
            pluginBinaryFile.setContent(actorProfile.getPhoto());
            pluginBinaryFile.persistToMedia();
        }

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
        PluginBinaryFile pluginBinaryFile = null;
        try {
            pluginBinaryFile = pluginFileSystem.getBinaryFile(pluginId, CONTACT_IMAGES_DIR, actorProfile.getIdentityPublicKey(), FilePrivacy.PUBLIC, FileLifeSpan.PERMANENT);
            pluginBinaryFile.loadFromMedia();
            actorProfile.setPhoto(pluginBinaryFile.getContent());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CantCreateFileException e) {
            e.printStackTrace();
        } catch (CantLoadFileException e) {
            e.printStackTrace();
        }
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
