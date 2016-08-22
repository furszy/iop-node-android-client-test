package org.iop.client.version_1.util;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.common.com.google.flatbuffers.FlatBufferBuilder;

import java.nio.ByteBuffer;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.PackageEncoder</code>
 * encode the package object to json string format
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 30/11/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class BlockEncoder implements Encoder.Binary<Package>{

    /**
     * (non-javadoc)
     * @see Text#encode(Object)
     */
    @Override
    public ByteBuffer encode(Package packageToSend) throws EncodeException {
        try {
            FlatBufferBuilder flatBufferBuilder = new FlatBufferBuilder();
            int packageId = flatBufferBuilder.createString(packageToSend.getPackageId().toString());
            int content = flatBufferBuilder.createString(packageToSend.getContent());
            int networkServiceType = flatBufferBuilder.createString(packageToSend.getNetworkServiceTypeSource().getCode());
            int pack = 0;
            if (packageToSend.getDestinationPublicKey()!=null) {
                int destinationPublicKey = flatBufferBuilder.createString(packageToSend.getDestinationPublicKey());
                pack = com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.common.Package.createPackage(
                        flatBufferBuilder,
                        packageId,
                        content,
                        packageToSend.getPackageType().getPackageTypeAsShort(),
                        networkServiceType,
                        destinationPublicKey);
            }else {

                pack = com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.common.Package.createPackage(
                        flatBufferBuilder,
                        packageId,
                        content,
                        packageToSend.getPackageType().getPackageTypeAsShort(),
                        networkServiceType,
                        0);
            }

            flatBufferBuilder.finish(pack);
            return flatBufferBuilder.dataBuffer();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {

    }

}
