package org.iop.ns.chat.structure;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseDataType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateTableException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.InvalidOwnerIdException;

import java.util.UUID;

public class ChatNetworkServiceDatabaseFactory {

    private final PluginDatabaseSystem pluginDatabaseSystem;
    private ErrorManager errorManager;

    public ChatNetworkServiceDatabaseFactory(final PluginDatabaseSystem pluginDatabaseSystem, ErrorManager errorManager) {

        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.errorManager = errorManager;
    }

    private void reportUnexpectedError(final Exception e) {
        errorManager.reportUnexpectedPluginException(Plugins.CHAT_NETWORK_SERVICE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
    }

    public Database createDatabase(UUID ownerId     ,
                                      String databaseName) throws CantCreateDatabaseException {

        try {

            Database database = this.pluginDatabaseSystem.createDatabase(
                    ownerId     ,
                    databaseName
            );

            DatabaseTableFactory table;
            DatabaseFactory databaseFactory = database.getDatabaseFactory();
            /**
             * Create Chat table.
             */
            table = databaseFactory.newTableFactory(ownerId, ChatNetworkServiceDataBaseConstants.MESSAGES_TABLE);

            table.addColumn(ChatNetworkServiceDataBaseConstants.MESSAGES_ID_COLUMN_NAME, DatabaseDataType.STRING, 150, Boolean.TRUE);
            table.addColumn(ChatNetworkServiceDataBaseConstants.MESSAGES_LOCAL_PM_COLUMN_NAME, DatabaseDataType.STRING, 255, Boolean.FALSE);
            table.addColumn(ChatNetworkServiceDataBaseConstants.MESSAGES_REMOTE_PK_COLUMN_NAME, DatabaseDataType.STRING, 255, Boolean.FALSE);
            table.addColumn(ChatNetworkServiceDataBaseConstants.MESSAGES_DATE_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0, Boolean.FALSE);
            table.addColumn(ChatNetworkServiceDataBaseConstants.MESSAGES_MESSAGE_COLUMN_NAME, DatabaseDataType.STRING, 4000, Boolean.FALSE);


            try {
                //Create the table
                databaseFactory.createTable(ownerId, table);

            } catch (CantCreateTableException cantCreateTableException) {
                CantCreateDatabaseException cantCreateDatabaseException = new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, cantCreateTableException, "", "Exception not handled by the plugin, There is a problem and i cannot create the table.");
                reportUnexpectedError(cantCreateDatabaseException);
                throw cantCreateDatabaseException;
            }catch(Exception e){
                CantCreateDatabaseException cantCreateDatabaseException = new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, e, "", e.getMessage());
                reportUnexpectedError(cantCreateDatabaseException);
                throw cantCreateDatabaseException;
            }


            /**
             * Create Chat table.
             */
            table = databaseFactory.newTableFactory(ownerId, ChatNetworkServiceDataBaseConstants.CONTACT_TABLE);

            table.addColumn(ChatNetworkServiceDataBaseConstants.CONTACT_ID_COLUMN_NAME, DatabaseDataType.STRING, 50, Boolean.TRUE);
            table.addColumn(ChatNetworkServiceDataBaseConstants.CONTACT_NAME_COLUMN_NAME, DatabaseDataType.STRING, 255, Boolean.FALSE);

            try {
                //Create the table
                databaseFactory.createTable(ownerId, table);

            } catch (CantCreateTableException cantCreateTableException) {
                CantCreateDatabaseException cantCreateDatabaseException = new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, cantCreateTableException, "", "Exception not handled by the plugin, There is a problem and i cannot create the table.");
                reportUnexpectedError(cantCreateDatabaseException);
                throw cantCreateDatabaseException;
            }catch(Exception e){
                CantCreateDatabaseException cantCreateDatabaseException = new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, e, "", e.getMessage());
                reportUnexpectedError(cantCreateDatabaseException);
                throw cantCreateDatabaseException;
            }

            return database;
        } catch (InvalidOwnerIdException invalidOwnerId) {
            /**
             * This shouldn't happen here because I was the one who gave the owner id to the database file system,
             * but anyway, if this happens, I can not continue.
             */
            CantCreateDatabaseException cantCreateDatabaseException = new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, invalidOwnerId, "", "There is a problem with the ownerId of the database.");
            reportUnexpectedError(cantCreateDatabaseException);
            throw cantCreateDatabaseException;
        }
    }
}

