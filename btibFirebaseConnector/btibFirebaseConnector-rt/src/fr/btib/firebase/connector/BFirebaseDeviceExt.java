package fr.btib.firebase.connector;

import fr.btib.connector.realtime.extension.BDeviceRealtimeConnectorExt;
import fr.btib.connector.realtime.interfaces.BIRealtimeConnector;

import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

@NiagaraType
public class BFirebaseDeviceExt extends BDeviceRealtimeConnectorExt
{
    /*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
    /*@ $com.btib.btibFirebaseConnector.BFirebaseDeviceExt(2979906276)1.0$ @*/
    /* Generated Thu Nov 07 16:30:23 CET 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

    ////////////////////////////////////////////////////////////////
    // Type
    ////////////////////////////////////////////////////////////////

    @Override
    public Type getType()
    {
        return TYPE;
    }

    public static final Type TYPE = Sys.loadType(BFirebaseDeviceExt.class);

    /*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    @Override
    public boolean filterConnector(BIRealtimeConnector connector)
    {
        return connector instanceof BFirebaseConnector;
    }
}