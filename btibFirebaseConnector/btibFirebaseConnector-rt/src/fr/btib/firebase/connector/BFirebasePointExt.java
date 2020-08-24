package fr.btib.firebase.connector;

import fr.btib.connector.realtime.extension.BPointRealtimeConnectorExt;
import fr.btib.connector.realtime.interfaces.BIRealtimeDeviceExtension;
import fr.btib.core.tool.BtibIconTool;

import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.BIcon;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

@NiagaraType
public class BFirebasePointExt extends BPointRealtimeConnectorExt
{
    /*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
    /*@ $fr.btib.firebase.connector.BFirebasePointExt(2979906276)1.0$ @*/
    /* Generated Sat Nov 09 18:22:45 CET 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

    ////////////////////////////////////////////////////////////////
    // Type
    ////////////////////////////////////////////////////////////////

    @Override
    public Type getType()
    {
        return TYPE;
    }

    public static final Type TYPE = Sys.loadType(BFirebasePointExt.class);

    /*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    private static final BIcon ICON = BtibIconTool.getComponentIcon(TYPE);

    @Override
    public boolean filterDeviceExt(BIRealtimeDeviceExtension extension)
    {
        return extension instanceof BFirebaseDeviceExt;
    }

    @Override
    public BIcon getIcon()
    {
        return ICON;
    }
}
