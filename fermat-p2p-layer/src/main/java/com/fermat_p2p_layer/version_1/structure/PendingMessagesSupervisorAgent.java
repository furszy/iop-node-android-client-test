package com.fermat_p2p_layer.version_1.structure;

import com.bitdubai.fermat_api.AbstractAgent;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by mati on 17/08/16.
 *  //todo: manuel ando cansado, no creo que esto esté bien pero igual lo dejo a ver si te dá una idea. mañana lo veo bien
 */
public class PendingMessagesSupervisorAgent extends AbstractAgent {

    private static final int MINIMUM_COUNT_TO_SEND_FULL_MESSAGE = 3;
    private static final int MEDIUM_COUNT = 15;
    private static final int SLEEP_COUNT = 40;



    private static final int SLEEP_TIME = 45;

    private int count;

    public PendingMessagesSupervisorAgent() {
        super("PenfingMessageSupervisor", SLEEP_TIME, TimeUnit.SECONDS, SLEEP_TIME);
    }

    @Override
    protected void agentJob() {

        count++;

        boolean haveToSearchMedium = false;
        boolean haveToSearchLarge = false;

        if (count==MEDIUM_COUNT){
            haveToSearchMedium = true;
        } else if (count==SLEEP_COUNT){
            haveToSearchMedium = true;
            haveToSearchLarge = true;
        }


        //obtener los datos de la db para enviar dependiendo de la cantidad de veces que se trató de enviar.
        // acordarse que ahora va a venir por el onMessageSentFail y no por acá ya que no hay un envio de paquete sincrono.



    }

    @Override
    protected void onErrorOccur(Exception e) {

    }


}
