package fr.btib.firebase.connector;

import fr.btib.connector.realtime.BRealtimeConnector;
import fr.btib.connector.realtime.alarm.recipient.BRealtimeAlarmRecipient;
import fr.btib.core.BtibLogger;
import fr.btib.core.tool.BtibIconTool;

import javax.baja.nre.annotations.Facet;
import javax.baja.nre.annotations.NiagaraProperty;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.*;

@NiagaraProperty(
    name = "connector",
    type = "String",
    defaultValue = "",
    facets = @Facet(name = "BFacets.FIELD_EDITOR", value = "\"btibFirebaseConnector:FirebaseRealtimeConnectorFE\"")
)
@NiagaraType
public class BFirebaseAlarmRecipient extends BRealtimeAlarmRecipient
{
    /*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
    /*@ $fr.btib.firebase.connector.BFirebaseAlarmRecipient(857153727)1.0$ @*/
    /* Generated Mon Dec 09 09:53:11 CET 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

    ////////////////////////////////////////////////////////////////
    // Property "connector"
    ////////////////////////////////////////////////////////////////

    /**
     * Slot for the {@code connector} property.
     *
     * @see #getConnector
     * @see #setConnector
     */
    public static final Property connector = newProperty(0, "", BFacets.make(BFacets.FIELD_EDITOR, "btibFirebaseConnector:FirebaseRealtimeConnectorFE"));

    /**
     * Get the {@code connector} property.
     *
     * @see #connector
     */
    @Override
    public String getConnector()
    {
        return getString(connector);
    }

    /**
     * Set the {@code connector} property.
     *
     * @see #connector
     */
    @Override
    public void setConnector(String v)
    {
        setString(connector, v, null);
    }

    ////////////////////////////////////////////////////////////////
    // Type
    ////////////////////////////////////////////////////////////////

    @Override
    public Type getType()
    {
        return TYPE;
    }

    public static final Type TYPE = Sys.loadType(BFirebaseAlarmRecipient.class);

    /*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    public static final BIcon ICON = BtibIconTool.getComponentIcon(TYPE);
    private static final BtibLogger LOG = BtibLogger.getLogger(TYPE);

    ////////////////////////////////////////////////////////////////
    // Overrides
    ////////////////////////////////////////////////////////////////

    @Override
    public boolean filterConnector(BRealtimeConnector connector)
    {
        return connector instanceof BFirebaseConnector;
    }

    @Override
    public BtibLogger getLogger()
    {
        return LOG;
    }

    ////////////////////////////////////////////////////////////////
    // Getters / Setters
    ////////////////////////////////////////////////////////////////

    @Override
    public BIcon getIcon()
    {
        return ICON;
    }
}
