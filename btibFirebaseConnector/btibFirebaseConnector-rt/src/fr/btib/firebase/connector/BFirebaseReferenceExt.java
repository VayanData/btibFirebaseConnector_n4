package fr.btib.firebase.connector;

import fr.btib.connector.realtime.extension.BRealtimeReferenceExt;
import fr.btib.connector.realtime.interfaces.BIRealtimeDeviceExtension;
import fr.btib.core.tool.BtibIconTool;

import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.BIcon;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

@NiagaraType
public class BFirebaseReferenceExt extends BRealtimeReferenceExt
{
    /*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
    /*@ $fr.btib.firebase.connector.BFirebaseReferenceExt(2979906276)1.0$ @*/
    /* Generated Tue Sep 08 09:51:48 CEST 2020 by Slot-o-Matic (c) Tridium, Inc. 2012 */

    ////////////////////////////////////////////////////////////////
    // Type
    ////////////////////////////////////////////////////////////////

    @Override
    public Type getType()
    {
        return TYPE;
    }

    public static final Type TYPE = Sys.loadType(BFirebaseReferenceExt.class);

    /*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    private static final BIcon ICON = BtibIconTool.getComponentIcon(TYPE);

    ////////////////////////////////////////////////////////////////
    // BRealtimeReferenceExt
    ////////////////////////////////////////////////////////////////

    @Override
    public boolean filterDeviceExt(BIRealtimeDeviceExtension extension)
    {
        return extension instanceof BFirebaseDeviceExt;
    }

    ////////////////////////////////////////////////////////////////
    // Setters / Getters
    ////////////////////////////////////////////////////////////////

    @Override
    public BIcon getIcon()
    {
        return ICON;
    }
}
