package com.fermat_p2p_layer.version_1.structure.database;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.entities.NetworkServiceMessage;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.enums.FermatMessagesStatus;
import com.fermat_p2p_layer.version_1.structure.exceptions.CantCheckPackageIdException;
import com.fermat_p2p_layer.version_1.structure.exceptions.CantGetNetworkServiceMessageException;
import com.fermat_p2p_layer.version_1.structure.exceptions.CantPersistsMessageException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.fermat_p2p_layer.version_1.structure.database.P2PLayerDatabaseConstants.P2P_LAYER_CONTENT_COLUMN_NAME;
import static com.fermat_p2p_layer.version_1.structure.database.P2PLayerDatabaseConstants.P2P_LAYER_DELIVERY_TIMESTAMP_COLUMN_NAME;
import static com.fermat_p2p_layer.version_1.structure.database.P2PLayerDatabaseConstants.P2P_LAYER_FAIL_COUNT_COLUMN_NAME;
import static com.fermat_p2p_layer.version_1.structure.database.P2PLayerDatabaseConstants.P2P_LAYER_FERMAT_MESSAGE_STATUS_COLUMN_NAME;
import static com.fermat_p2p_layer.version_1.structure.database.P2PLayerDatabaseConstants.P2P_LAYER_IS_BETWEEN_ACTORS_COLUMN_NAME;
import static com.fermat_p2p_layer.version_1.structure.database.P2PLayerDatabaseConstants.P2P_LAYER_MESSAGES_TABLE_NAME;
import static com.fermat_p2p_layer.version_1.structure.database.P2PLayerDatabaseConstants.P2P_LAYER_NETWORK_SERVICE_TYPE_COLUMN_NAME;
import static com.fermat_p2p_layer.version_1.structure.database.P2PLayerDatabaseConstants.P2P_LAYER_PACKAGE_ID_COLUMN_NAME;
import static com.fermat_p2p_layer.version_1.structure.database.P2PLayerDatabaseConstants.P2P_LAYER_RECEIVER_PUBLIC_KEY_COLUMN_NAME;
import static com.fermat_p2p_layer.version_1.structure.database.P2PLayerDatabaseConstants.P2P_LAYER_SENDER_PUBLIC_KEY_COLUMN_NAME;
import static com.fermat_p2p_layer.version_1.structure.database.P2PLayerDatabaseConstants.P2P_LAYER_SHIPPING_TIMESTAMP_COLUMN_NAME;
import static com.fermat_p2p_layer.version_1.structure.database.P2PLayerDatabaseConstants.P2P_LAYER_SIGNATURE_COLUMN_NAME;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 18/08/16.
 */
public class P2PLayerDao {

    /**
     * Represents the plugin database
     */
    Database database;

    /**
     * Constructor
     */
    public P2PLayerDao(
            Database database) throws CantOpenDatabaseException {
        this.database = database;
    }

    /**
     * This method returns the default database table
     * @return
     */
    private DatabaseTable getDatabaseTable() {
        return database.getTable(P2P_LAYER_MESSAGES_TABLE_NAME);
    }

    /**
     * This method persists a NetworkServiceMessage in database
     * @param networkServiceMessage
     * @throws CantPersistsMessageException
     */
    public void persistMessage(
            NetworkServiceMessage networkServiceMessage) throws CantPersistsMessageException {

        try{
            DatabaseTable databaseTable = getDatabaseTable();
            DatabaseTableRecord databaseTableRecord = databaseTable.getEmptyRecord();
            //Build database table record
            databaseTableRecord = buildDatabaseRecord(networkServiceMessage, databaseTableRecord);
            //Check if the message is persisted in database
            if(existsPackageId(networkServiceMessage.getId())){
                //The message exists, I'll increase the fail count in 1 unit and update the record
                networkServiceMessage.setFailCount(networkServiceMessage.getFailCount()+1);
                databaseTable.updateRecord(databaseTableRecord);
            } else{
                //Insert record
                databaseTable.insertRecord(databaseTableRecord);
            }

        } catch (CantInsertRecordException e) {
            throw new CantPersistsMessageException(
                    e,
                    "Persisting a message in database",
                    "Cannot insert record");
        } catch (CantCheckPackageIdException e) {
            throw new CantPersistsMessageException(
                    e,
                    "Persisting a message in database",
                    "Cannot check if the message exists in database");
        } catch (CantUpdateRecordException e) {
            throw new CantPersistsMessageException(
                    e,
                    "Persisting a message in database",
                    "Cannot update record");
        } catch (Exception e){
            throw new CantPersistsMessageException(
                    e,
                    "Persisting a message in database",
                    "Unexpected exception");
        }

    }

    /**
     * This method builds a database table record
     * @param networkServiceMessage
     * @param databaseTableRecord
     * @return
     */
    private DatabaseTableRecord buildDatabaseRecord(
            NetworkServiceMessage networkServiceMessage,
            DatabaseTableRecord databaseTableRecord){
        //Package Id
        databaseTableRecord.setUUIDValue(
                P2P_LAYER_PACKAGE_ID_COLUMN_NAME,
                networkServiceMessage.getId());
        //Message content
        databaseTableRecord.setStringValue(
                P2P_LAYER_PACKAGE_ID_COLUMN_NAME,
                networkServiceMessage.getContent());
        //Network Service Type
        databaseTableRecord.setFermatEnum(
                P2P_LAYER_NETWORK_SERVICE_TYPE_COLUMN_NAME,
                networkServiceMessage.getNetworkServiceType());
        //Sender Public Key
        databaseTableRecord.setStringValue(
                P2P_LAYER_SENDER_PUBLIC_KEY_COLUMN_NAME,
                networkServiceMessage.getSenderPublicKey());
        //Receiver Public key
        databaseTableRecord.setStringValue(
                P2P_LAYER_RECEIVER_PUBLIC_KEY_COLUMN_NAME,
                networkServiceMessage.getReceiverPublicKey());
        //Shipping timestamp
        databaseTableRecord.setStringValue(
                P2P_LAYER_SHIPPING_TIMESTAMP_COLUMN_NAME,
                networkServiceMessage.getShippingTimestamp().toString());
        //Delivery timestamp
        databaseTableRecord.setStringValue(
                P2P_LAYER_DELIVERY_TIMESTAMP_COLUMN_NAME,
                networkServiceMessage.getDeliveryTimestamp().toString());
        //Is Between actors flag
        databaseTableRecord.setStringValue(
                P2P_LAYER_IS_BETWEEN_ACTORS_COLUMN_NAME,
                networkServiceMessage.isBetweenActors().toString());
        //Message status
        databaseTableRecord.setFermatEnum(
                P2P_LAYER_FERMAT_MESSAGE_STATUS_COLUMN_NAME,
                networkServiceMessage.getFermatMessagesStatus());
        //Message signature
        databaseTableRecord.setStringValue(
                P2P_LAYER_SIGNATURE_COLUMN_NAME,
                networkServiceMessage.getSignature());
        //Fail count
        databaseTableRecord.setIntegerValue(
                P2P_LAYER_FAIL_COUNT_COLUMN_NAME,
                networkServiceMessage.getFailCount());

        return databaseTableRecord;
    }

    /**
     * This method returns a NetworkServiceMessage from DatabaseTableRecord
     * @param databaseTableRecord
     * @return
     */
    private NetworkServiceMessage buildNetworkServiceMessage(DatabaseTableRecord databaseTableRecord){

        NetworkServiceMessage networkServiceMessage = new NetworkServiceMessage();
        //Package Id
        networkServiceMessage.setId(databaseTableRecord.getUUIDValue(P2P_LAYER_PACKAGE_ID_COLUMN_NAME));
        //Message content
        networkServiceMessage.setContent(databaseTableRecord.getStringValue(P2P_LAYER_CONTENT_COLUMN_NAME));
        //Network Service type
        String nsTypeString = databaseTableRecord.getStringValue(P2P_LAYER_NETWORK_SERVICE_TYPE_COLUMN_NAME);
        NetworkServiceType networkServiceType;
        if(nsTypeString==null||nsTypeString.isEmpty()){
            //The value is empty, I'll set as UNDEFINED
            networkServiceType = NetworkServiceType.UNDEFINED;
        } else {
            try{
                networkServiceType = NetworkServiceType.getByCode(nsTypeString);
            } catch (InvalidParameterException e){
                //The code is invalid, I'll set as UNDEFINED
                networkServiceType = NetworkServiceType.UNDEFINED;
            }
        }
        networkServiceMessage.setNetworkServiceType(networkServiceType);
        //Sender public key
        networkServiceMessage.setSenderPublicKey(
                databaseTableRecord.getStringValue(P2P_LAYER_SENDER_PUBLIC_KEY_COLUMN_NAME));
        //Receiver public key
        networkServiceMessage.setReceiverPublicKey(
                databaseTableRecord.getStringValue(P2P_LAYER_RECEIVER_PUBLIC_KEY_COLUMN_NAME));
        //Shipping timestamp
        Long stLong = databaseTableRecord.getLongValue(P2P_LAYER_SHIPPING_TIMESTAMP_COLUMN_NAME);
        Timestamp shippingTimestamp;
        if(stLong==null){
            shippingTimestamp = new Timestamp(0);
        } else{
            shippingTimestamp = new Timestamp(stLong);
        }
        networkServiceMessage.setDeliveryTimestamp(shippingTimestamp);
        //Delivery timestamp
        Long dtLong = databaseTableRecord.getLongValue(P2P_LAYER_DELIVERY_TIMESTAMP_COLUMN_NAME);
        Timestamp deliveryTimestamp;
        if(dtLong==null){
            deliveryTimestamp = new Timestamp(0);
        } else{
            deliveryTimestamp = new Timestamp(dtLong);
        }
        networkServiceMessage.setDeliveryTimestamp(deliveryTimestamp);
        //Is between actors
        String ibaString = databaseTableRecord.getStringValue(P2P_LAYER_IS_BETWEEN_ACTORS_COLUMN_NAME);
        Boolean isBetweenActors;
        if(ibaString==null||ibaString.isEmpty()){
            isBetweenActors=Boolean.FALSE;
        } else{
            isBetweenActors = Boolean.parseBoolean(ibaString);
        }
        networkServiceMessage.setIsBetweenActors(isBetweenActors);
        //Fermat Message Status
        String fmString = databaseTableRecord.getStringValue(P2P_LAYER_FERMAT_MESSAGE_STATUS_COLUMN_NAME);
        FermatMessagesStatus fermatMessagesStatus;
        if(fmString==null||fmString.isEmpty()){
            //The value is empty, I'll set as FAILED
            fermatMessagesStatus = FermatMessagesStatus.FAILED;
        } else {
            try{
                fermatMessagesStatus = FermatMessagesStatus.getByCode(fmString);
            } catch (InvalidParameterException e){
                //The code is invalid, I'll set as UNDEFINED
                fermatMessagesStatus = FermatMessagesStatus.FAILED;
            }
        }
        networkServiceMessage.setFermatMessagesStatus(fermatMessagesStatus);
        //Signature
        networkServiceMessage.setSignature(databaseTableRecord.getStringValue(P2P_LAYER_SIGNATURE_COLUMN_NAME));
        //Fail count
        networkServiceMessage.setFailCount(databaseTableRecord.getIntegerValue(P2P_LAYER_FAIL_COUNT_COLUMN_NAME));

        return networkServiceMessage;

    }

    /**
     * This method checks if a package exists in database
     * @param packageId
     * @return
     * @throws CantCheckPackageIdException
     */
    public boolean existsPackageId(UUID packageId) throws CantCheckPackageIdException {

        DatabaseTable table = getDatabaseTable();
        //Set filter
        table.addUUIDFilter(P2P_LAYER_PACKAGE_ID_COLUMN_NAME, packageId, DatabaseFilterType.EQUAL);
        try{
            table.loadToMemory();
            List<DatabaseTableRecord> records = table.getRecords();
            return !records.isEmpty();
        } catch (CantLoadTableToMemoryException e) {
            throw new CantCheckPackageIdException(
                    e,
                    "Checking if a package Id exists in database",
                    "Cannot load database table into memory");
        } catch (Exception e){
            throw new CantCheckPackageIdException(
                    e,
                    "Checking if a package Id exists in database",
                    "Unexpected exception");
        }

    }

    /**
     * This method returns a NetworkServiceMessage from database
     * @param packageId
     * @return
     * @throws CantGetNetworkServiceMessageException
     */
    public NetworkServiceMessage getNetworkServiceMessageById(UUID packageId)
            throws CantGetNetworkServiceMessageException {
        DatabaseTable table = getDatabaseTable();
        //Set filter
        table.addUUIDFilter(P2P_LAYER_PACKAGE_ID_COLUMN_NAME, packageId, DatabaseFilterType.EQUAL);
        try{
            table.loadToMemory();
            List<DatabaseTableRecord> records = table.getRecords();
            if(records.isEmpty()){
                //Cannot find the record in database
                return null;
            } else{
                DatabaseTableRecord record = records.get(0);
                return buildNetworkServiceMessage(record);
            }

        } catch (CantLoadTableToMemoryException e) {
            throw new CantGetNetworkServiceMessageException(
                    e,
                    "Getting NetworkServiceMessage from database",
                    "Cannot load database table into memory");
        } catch (Exception e){
            throw new CantGetNetworkServiceMessageException(
                    e,
                    "Getting NetworkServiceMessage from database",
                    "Unexpected exception");
        }
    }

    /**
     * This method returns a NetworkServiceMessage list from database by the fail count value
     * @param failCount
     * @return
     * @throws CantGetNetworkServiceMessageException
     */
    public List<NetworkServiceMessage> getNetworkServiceMessageByFailCount(int failCount)
            throws CantGetNetworkServiceMessageException {
        DatabaseTable table = getDatabaseTable();
        //Set filter
        table.addStringFilter(P2P_LAYER_PACKAGE_ID_COLUMN_NAME, failCount+"", DatabaseFilterType.EQUAL);
        try{
            table.loadToMemory();
            List<DatabaseTableRecord> records = table.getRecords();
            if(records.isEmpty()){
                //Cannot find the record in database
                return new ArrayList<>();
            } else{
                List<NetworkServiceMessage> networkServiceMessages = new ArrayList<>();
                for (DatabaseTableRecord record : records) {
                    networkServiceMessages.add(buildNetworkServiceMessage(record));
                }
                records=null;
                return networkServiceMessages;
            }

        } catch (CantLoadTableToMemoryException e) {
            throw new CantGetNetworkServiceMessageException(
                    e,
                    "Getting NetworkServiceMessage from database by fail count",
                    "Cannot load database table into memory");
        } catch (Exception e){
            throw new CantGetNetworkServiceMessageException(
                    e,
                    "Getting NetworkServiceMessage from database by fail count",
                    "Unexpected exception");
        }
    }

}
