package fr.btib.firebase.connector.ui;

import fr.btib.connector.BExternalConnector;
import fr.btib.connector.wb.ui.element.connector.realtime.BRealtimeConnectorFE;
import fr.btib.core.tool.BtibIconTool;
import fr.btib.firebase.connector.BFirebaseConnector;

import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.BIcon;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

@NiagaraType
public class BFirebaseRealtimeConnectorFE extends BRealtimeConnectorFE
{
    /*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
    /*@ $fr.btib.firebase.connector.ui.BFirebaseRealtimeConnectorFE(2979906276)1.0$ @*/
    /* Generated Mon Nov 11 12:34:09 CET 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

    ////////////////////////////////////////////////////////////////
    // Type
    ////////////////////////////////////////////////////////////////

    @Override
    public Type getType()
    {
        return TYPE;
    }

    public static final Type TYPE = Sys.loadType(BFirebaseRealtimeConnectorFE.class);

    /*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    public static final BIcon ICON = BtibIconTool.getComponentIcon(TYPE);

    ////////////////////////////////////////////////////////////////
    // Overrides
    ////////////////////////////////////////////////////////////////

    @Override
    public boolean isConnectorValid(BExternalConnector connector)
    {
        return super.isConnectorValid(connector) && connector instanceof BFirebaseConnector;
    }
}
