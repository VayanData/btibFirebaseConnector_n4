package fr.btib.firebase.connector;

import fr.btib.core.license.inherit.BFrozenEnumBtib;

import javax.baja.nre.annotations.NiagaraEnum;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.nre.annotations.Range;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

@NiagaraType
@NiagaraEnum(
    range = {
        @Range("alarmsCollection"),
        @Range("devicesCollection"),
        @Range("pointsStatusCollection"),
        @Range("pointsValuesCollection")
    }
)
public final class BDataDestinationEnum extends BFrozenEnumBtib
{
    /*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
    /*@ $fr.btib.firebase.connector.BDataDestinationEnum(3747253359)1.0$ @*/
    /* Generated Mon Aug 24 17:20:12 CEST 2020 by Slot-o-Matic (c) Tridium, Inc. 2012 */

    /**
     * Ordinal value for alarmsCollection.
     */
    public static final int ALARMS_COLLECTION = 0;
    /**
     * Ordinal value for devicesCollection.
     */
    public static final int DEVICES_COLLECTION = 1;
    /**
     * Ordinal value for pointsStatusCollection.
     */
    public static final int POINTS_STATUS_COLLECTION = 2;
    /**
     * Ordinal value for pointsValuesCollection.
     */
    public static final int POINTS_VALUES_COLLECTION = 3;

    /**
     * BDataDestinationEnum constant for alarmsCollection.
     */
    public static final BDataDestinationEnum alarmsCollection = new BDataDestinationEnum(ALARMS_COLLECTION);
    /**
     * BDataDestinationEnum constant for devicesCollection.
     */
    public static final BDataDestinationEnum devicesCollection = new BDataDestinationEnum(DEVICES_COLLECTION);
    /**
     * BDataDestinationEnum constant for pointsStatusCollection.
     */
    public static final BDataDestinationEnum pointsStatusCollection = new BDataDestinationEnum(POINTS_STATUS_COLLECTION);
    /**
     * BDataDestinationEnum constant for pointsValuesCollection.
     */
    public static final BDataDestinationEnum pointsValuesCollection = new BDataDestinationEnum(POINTS_VALUES_COLLECTION);

    /**
     * Factory method with ordinal.
     */
    public static BDataDestinationEnum make(int ordinal)
    {
        return (BDataDestinationEnum) alarmsCollection.getRange().get(ordinal, false);
    }

    /**
     * Factory method with tag.
     */
    public static BDataDestinationEnum make(String tag)
    {
        return (BDataDestinationEnum) alarmsCollection.getRange().get(tag);
    }

    /**
     * Private constructor.
     */
    private BDataDestinationEnum(int ordinal)
    {
        super(ordinal);
    }

    public static final BDataDestinationEnum DEFAULT = alarmsCollection;

    ////////////////////////////////////////////////////////////////
    // Type
    ////////////////////////////////////////////////////////////////

    @Override
    public Type getType()
    {
        return TYPE;
    }

    public static final Type TYPE = Sys.loadType(BDataDestinationEnum.class);

    /*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/
}
