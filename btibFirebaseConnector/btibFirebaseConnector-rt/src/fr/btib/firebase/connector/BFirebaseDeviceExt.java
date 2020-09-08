package fr.btib.firebase.connector;

import fr.btib.connector.realtime.extension.BDeviceRealtimeConnectorExt;
import fr.btib.connector.realtime.interfaces.BIRealtimeConnector;
import fr.btib.core.tool.BtibIconTool;

import javax.baja.nre.annotations.Facet;
import javax.baja.nre.annotations.NiagaraProperty;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.*;

@NiagaraProperty(
    name = "connector",
    type = "String",
    defaultValue = "",
    facets = @Facet(name = "BFacets.FIELD_EDITOR", value = "\"btibFirebaseConnector:BFirebaseRealtimeConnectorFE\""),
    override = true
)
@NiagaraType
public class BFirebaseDeviceExt extends BDeviceRealtimeConnectorExt
{
    /*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
    /*@ $fr.btib.firebase.connector.BFirebaseDeviceExt(1509121537)1.0$ @*/
    /* Generated Mon Nov 11 12:36:09 CET 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

    ////////////////////////////////////////////////////////////////
    // Property "connector"
    ////////////////////////////////////////////////////////////////

    /**
     * Slot for the {@code connector} property.
     *
     * @see #getConnector
     * @see #setConnector
     */
    public static final Property connector = newProperty(0, "", BFacets.make(BFacets.FIELD_EDITOR, "btibFirebaseConnector:BFirebaseRealtimeConnectorFE"));

    /**
     * Get the {@code connector} property.
     *
     * @see #connector
     */
    public String getConnector()
    {
        return this.getString(connector);
    }

    /**
     * Set the {@code connector} property.
     *
     * @see #connector
     */
    public void setConnector(String v)
    {
        this.setString(connector, v, null);
    }

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

    private static final BIcon ICON = BtibIconTool.getComponentIcon(TYPE);

    @Override
    public boolean filterConnector(BIRealtimeConnector connector)
    {
        return connector instanceof BFirebaseConnector;
    }

    @Override
    public BIcon getIcon()
    {
        return ICON;
    }
}
