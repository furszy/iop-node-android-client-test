package org.iop.client.version_1.util;


import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;

import java.nio.ByteBuffer;
import java.util.UUID;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * <p/>
 * Created by Matias Furszyfer
 *
 * @version 1.0
 * */
public class PackageDecoder implements Decoder.Binary<Package>{

    /**
     * (non-javadoc)
     * @see Text#init(EndpointConfig)
     */
    @Override
    public void init(EndpointConfig config) {

    }

    /**
     * (non-javadoc)
     * @see Text#destroy()
     */
    @Override
    public void destroy() {

    }

    @Override
    public Package decode(ByteBuffer bytes) throws DecodeException {
        com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.common.Package pack = com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.common.Package.getRootAsPackage(bytes);

        return Package.rebuildInstance(
                UUID.fromString(pack.id()),
                pack.content(),
                NetworkServiceType.getByCode(pack.networkServiceType()),
                PackageType.buildWithInt(pack.packageType()),
                pack.destinationPk()
        );
    }

    @Override
    public boolean willDecode(ByteBuffer bytes) {
        return true;
    }




}
