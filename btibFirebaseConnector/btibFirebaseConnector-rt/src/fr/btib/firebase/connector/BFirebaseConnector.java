package fr.btib.firebase.connector;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.tridium.json.JSONObject;
import fr.btib.connector.exception.ConnectorDisabledException;
import fr.btib.connector.interfaces.RunnableThrowable;
import fr.btib.connector.realtime.BRealtimeConnector;
import fr.btib.connector.realtime.interfaces.BIIncomingMessageListener;
import fr.btib.connector.realtime.interfaces.BIRealtimeDeviceExtension;
import fr.btib.connector.realtime.interfaces.BIRealtimePointExtension;
import fr.btib.connector.realtime.messages.incoming.IIncomingMessage;
import fr.btib.connector.realtime.messages.outgoing.OutgoingPointMessage;
import fr.btib.core.BtibLogger;
import fr.btib.core.tool.BtibIconTool;
import fr.btib.core.tool.CompTool;

import javax.annotation.Nullable;
import javax.baja.nre.annotations.NiagaraProperty;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.*;
import java.io.ByteArrayInputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@NiagaraType
@NiagaraProperty(
    name = "firebaseKeyJson",
    type = "String",
    defaultValue = ""
)
public class BFirebaseConnector extends BRealtimeConnector
{
    /*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
    /*@ $fr.btib.firebase.connector.BFirebaseConnector(157606577)1.0$ @*/
    /* Generated Fri Nov 08 10:36:55 CET 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

    ////////////////////////////////////////////////////////////////
    // Property "firebaseKeyJson"
    ////////////////////////////////////////////////////////////////

    /**
     * Slot for the {@code firebaseKeyJson} property.
     *
     * @see #getFirebaseKeyJson
     * @see #setFirebaseKeyJson
     */
    public static final Property firebaseKeyJson = newProperty(0, "", null);

    /**
     * Get the {@code firebaseKeyJson} property.
     *
     * @see #firebaseKeyJson
     */
    public String getFirebaseKeyJson()
    {
        return this.getString(firebaseKeyJson);
    }

    /**
     * Set the {@code firebaseKeyJson} property.
     *
     * @see #firebaseKeyJson
     */
    public void setFirebaseKeyJson(String v)
    {
        this.setString(firebaseKeyJson, v, null);
    }

    ////////////////////////////////////////////////////////////////
    // Type
    ////////////////////////////////////////////////////////////////

    @Override
    public Type getType()
    {
        return TYPE;
    }

    public static final Type TYPE = Sys.loadType(BFirebaseConnector.class);

    /*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/
    // the logger
    private static final BtibLogger LOG = BtibLogger.getLogger(TYPE);
    // Use the icon on the icon resource
    private static final BIcon ICON = BtibIconTool.getComponentIcon(TYPE);
    // the firebase url
    private static final String DATABASE_URL = "https://realtimeconnectordemo.firebaseio.com";
    private static final String DEVICES_COLLECTION = "devices";
    private static final String ALARMS_COLLECTION = "alarms";
    private static final String POINTS_VALUES_COLLECTION = "pointsValues";
    private static final String POINTS_STATUSES_COLLECTION = "pointsStatus";
    private static final String MESSAGES_COLLECTION = "messages";
    // the workers thread pool
    private final ExecutorService executorService = AccessController.doPrivileged((PrivilegedAction<ExecutorService>) Executors::newCachedThreadPool);
    // Devices incoming messages listeners
    private final Map<String, Set<BIIncomingMessageListener>> listeners = new HashMap<>();
    private FirebaseApp firebaseApp = null;
    private Firestore firestore = null;
    private ListenerRegistration messageListenerRegistration;

    ////////////////////////////////////////////////////////////////
    // BExternalConnector
    ////////////////////////////////////////////////////////////////

    @Override
    public void doPing()
    {
        this.setLastAttempt(BAbsTime.now());
        this.pingService()
            .exceptionally(e -> {
                CompTool.setFault(this, e.getMessage(), e, LOG);
                this.setLastFailure(BAbsTime.now());
                return null;
            })
            .thenRun(() -> {
                CompTool.setOk(this);
                this.setLastSuccess(BAbsTime.now());
            });
    }

    ////////////////////////////////////////////////////////////////
    // BrealtimeConnector
    ////////////////////////////////////////////////////////////////

    @Override
    public BFrozenEnum getDeviceTagsDestination()
    {
        return BDataDestinationEnum.devicesCollection;
    }

    @Override
    public BFrozenEnum getDeviceAlarmDestination()
    {
        return BDataDestinationEnum.alarmsCollection;
    }

    @Override
    public BFrozenEnum getPointTagsDestination()
    {
        return BDataDestinationEnum.pointsValuesCollection;
    }

    @Override
    public BFrozenEnum getPointStatusDestination()
    {
        return BDataDestinationEnum.pointsStatusCollection;
    }

    @Override
    public BFrozenEnum getPointValueDestination()
    {
        return BDataDestinationEnum.pointsValuesCollection;
    }

    @Override
    public ExecutorService getRegistryExecutorService()
    {
        return this.executorService;
    }

    @Override
    public ExecutorService getConnectionExecutorService()
    {
        return this.executorService;
    }

    ////////////////////////////////////////////////////////////////
    // BIRealtimeConnector
    ////////////////////////////////////////////////////////////////

    @Override
    public Class<? extends BIRealtimeDeviceExtension> getDeviceExtensionClass()
    {
        return BFirebaseDeviceExt.class;
    }

    @Override
    public Class<? extends BIRealtimePointExtension> getPointExtensionClass()
    {
        return BFirebasePointExt.class;
    }

    @Override
    public CompletableFuture<Void> registerDevice(String deviceId)
    {
        return this.doAsync(() -> this.getFirestore().collection(DEVICES_COLLECTION).document(deviceId).set(new HashMap<>()).get());
    }

    @Override
    public CompletableFuture<Void> registerPoint(String deviceId, String pointId)
    {
        return this.doAsync(() -> {
            this.getFirestore().collection(POINTS_VALUES_COLLECTION).document(pointId).set(new HashMap<>()).get();
            this.getFirestore().collection(POINTS_STATUSES_COLLECTION).document(pointId).set(new HashMap<>()).get();
        });
    }

    @Override
    public CompletableFuture<Void> unregisterDevice(String deviceId)
    {
        return this.doAsync(() -> {
            this.getFirestore().collection(DEVICES_COLLECTION).document(deviceId).delete().get();
        });
    }

    @Override
    public CompletableFuture<Void> unregisterPoint(String deviceId, String pointId)
    {
        return this.doAsync(() -> {
            this.getFirestore().collection(POINTS_VALUES_COLLECTION).document(pointId).delete().get();
            this.getFirestore().collection(POINTS_STATUSES_COLLECTION).document(pointId).delete().get();
        });
    }

    @Override
    public CompletableFuture<Void> openConnectionForDevice(String deviceId)
    {
        return this.doAsync(this::getFirestore);
    }

    @Override
    public CompletableFuture<Void> closeConnectionForDevice(String deviceId)
    {
        // Don't need to close the firebase connection
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public boolean isDeviceConnected(String deviceId)
    {
        return this.firestore != null;
    }

    @Override
    public CompletableFuture<Void> sendTagsForDevice(String deviceId, Map<String, String> tags)
    {
        return this.doAsync(() -> {
            this.clearNulls(tags);
            Map<String, Object> fields = new HashMap<>();
            fields.put("tags", tags);
            this.getFirestore().collection(getDestination(DEVICE_TAGS_DESTINATION_SLOT)).document(deviceId).set(fields).get();
        });
    }

    @Override
    public CompletableFuture<Void> sendTagsForPoint(String deviceId, String pointId, Map<String, String> tags)
    {
        return this.doAsync(() -> {
            this.clearNulls(tags);
            Map<String, Object> fields = new HashMap<>();
            fields.put("tags", tags);
            this.getFirestore().collection(this.getDestination(POINT_TAGS_DESTINATION_SLOT)).document(pointId).update(fields).get();
        });
    }

    @Override
    public CompletableFuture<Void> sendMessage(String deviceId, OutgoingPointMessage message)
    {
        return this.doAsync(() -> {
            JSONObject jsonObject = new JSONObject(message.getMessageString());
            if (!jsonObject.has(POINT_ID_VARIABLE))
            {
                throw new Exception("the pointId field is required in the json message");
            }
            String pointId = jsonObject.getString(POINT_ID_VARIABLE);

            Map<String, Object> data = new HashMap<>();
            jsonObject.keys().forEachRemaining(key -> data.put((String) key, jsonObject.get((String) key)));
            this.getFirestore().collection(this.getDestination(POINT_VALUE_DESTINATION_SLOT)).document(pointId).update(data).get();
        });
    }

    @Override
    public CompletableFuture<Void> sendState(String deviceId, OutgoingPointMessage message)
    {
        return this.doAsync(() -> {
            JSONObject jsonObject = new JSONObject(message.getMessageString());
            if (!jsonObject.has(POINT_ID_VARIABLE))
            {
                throw new Exception("the pointId field is required in the json message");
            }
            String pointId = jsonObject.getString(POINT_ID_VARIABLE);

            Map<String, Object> data = new HashMap<>();
            jsonObject.keys().forEachRemaining(key -> data.put((String) key, jsonObject.get((String) key)));
            this.getFirestore().collection(this.getDestination(POINT_STATUS_DESTINATION_SLOT)).document(pointId).update(data).get();
        });
    }

    @Override
    public CompletableFuture<Void> sendAlarm(String deviceId, OutgoingPointMessage message)
    {
        return this.doAsync(() -> {
            JSONObject jsonObject = new JSONObject(message.getMessageString());
            Map<String, Object> data = new HashMap<>();
            jsonObject.keys().forEachRemaining(key -> data.put((String) key, jsonObject.get((String) key)));
            this.getFirestore().collection(getDestination(DEVICE_ALARM_DESTINATION_SLOT)).add(data).get();
        });
    }

    @Override
    public void subscribeToIncomingMessages(String deviceId, BIIncomingMessageListener incomingMessageListener) throws Exception
    {
        if (!this.listeners.containsKey(deviceId))
        {
            this.listeners.put(deviceId, new HashSet<>());
        }
        this.listeners.get(deviceId).add(incomingMessageListener);
    }

    @Override
    public void unsubscribeToIncomingMessages(String deviceId, BIIncomingMessageListener incomingMessageListener)
    {
        this.listeners.remove(deviceId);
    }

    @Override
    public CompletableFuture<Void> pingService()
    {
        return this.doAsync(() -> this.getFirestore().getCollections().forEach(CollectionReference::getId));
    }

    @Override
    public BtibLogger getBtibLogger()
    {
        return LOG;
    }

    @Override
    public void started() throws Exception
    {
        super.started();
        // check if the station is already started when you drag and drop the component for the first time
        if (!Sys.isStationStarted())
        {
            // When station is booting
            this.firebaseApp = this.initializeFirebase();
        }
    }

    @Override
    public void changed(Property property, Context context)
    {
        if (!this.isRunning())
        {
            return;
        }

        if (property == firebaseKeyJson)
        {
            try
            {
                this.firestore = null;
                this.firebaseApp = this.initializeFirebase();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    ////////////////////////////////////////////////////////////////
    // Utils
    ////////////////////////////////////////////////////////////////

    /**
     * Gets data destination
     *
     * @param slot
     * @return
     */
    private String getDestination(String slot)
    {
        BDataDestinationEnum tagsDestination = (BDataDestinationEnum) this.getConfigSlot(slot);
        if (tagsDestination == BDataDestinationEnum.pointsValuesCollection)
        {
            return POINTS_VALUES_COLLECTION;
        }
        if (tagsDestination == BDataDestinationEnum.pointsStatusCollection)
        {
            return POINTS_STATUSES_COLLECTION;
        }
        if (tagsDestination == BDataDestinationEnum.devicesCollection)
        {
            return DEVICES_COLLECTION;
        }
        if (tagsDestination == BDataDestinationEnum.alarmsCollection)
        {
            return ALARMS_COLLECTION;
        }
        return "unknown";
    }

    /**
     * Remove null values
     *
     * @param data
     * @param <T>
     */
    private <T> void clearNulls(Map<String, T> data)
    {
        List<String> nullKeys = data.entrySet().stream()
            .filter(pair -> pair.getValue() == null)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        nullKeys.forEach(data::remove);
    }

    /**
     * Create the firestore client   lazily
     *
     * @return
     * @throws Exception
     */
    private Firestore getFirestore() throws Exception
    {
        if (this.firestore == null)
        {
            if (this.firebaseApp == null)
            {
                this.firebaseApp = this.initializeFirebase();
            }
            if (this.messageListenerRegistration != null)
            {
                this.messageListenerRegistration.remove();
            }
            this.firestore = AccessController.doPrivileged((PrivilegedAction<Firestore>) () -> {
                try
                {
                    return FirestoreClient.getFirestore();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return null;
                }
            });
            this.listenForMessages();
        }
        return this.firestore;
    }

    /**
     * Start listening for messages
     */
    private void listenForMessages()
    {
        this.messageListenerRegistration = this.firestore.collection(MESSAGES_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirestoreException error)
            {
                if (snapshots != null)
                {
                    snapshots.getDocumentChanges().forEach(document -> {
                        try
                        {
                            String jsonMessage = new JSONObject(document.getDocument().getData()).toString();
                            List<IIncomingMessage> messages = BFirebaseConnector.this.getMessageExtractor().getMessages(jsonMessage);
                            for (IIncomingMessage message : messages)
                            {
                                if (message.getDeviceId() == null)
                                {
                                    BFirebaseConnector.this.getBLog().copy().eventName("IncomingMessage").failed().message("Device id is required for message: " + message.toString()).send();
                                }
                                if (BFirebaseConnector.this.listeners.containsKey(message.getDeviceId()))
                                {
                                    BFirebaseConnector.this.listeners.get(message.getDeviceId()).forEach(listener -> listener.handleIncomingMessage(message));
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            LOG.severe(e.getMessage());
                        }
                    });
                }
            }
        });
    }

    /**
     * Init the firebase application instance
     *
     * @return
     * @throws Exception
     */
    private FirebaseApp initializeFirebase() throws Exception
    {
        AtomicReference<Exception> innerException = new AtomicReference<>();
        FirebaseApp app = AccessController.doPrivileged((PrivilegedAction<FirebaseApp>) () -> {
            try
            {
                ByteArrayInputStream credentialsStream = new ByteArrayInputStream(this.getFirebaseKeyJson().getBytes());
                FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .setDatabaseUrl(DATABASE_URL)
                    .build();
                return FirebaseApp.initializeApp(options);
            }
            catch (Exception exception)
            {
                innerException.set(exception);
                return null;
            }
        });
        if (innerException.get() != null)
        {
            throw innerException.get();
        }
        return app;
    }

    /**
     * Submit the task an returns the future result
     *
     * @param runnableThrowable
     * @return
     */
    private CompletableFuture<Void> doAsync(RunnableThrowable runnableThrowable)
    {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (!this.getEnabled())
        {
            future.completeExceptionally(new ConnectorDisabledException());
        }
        this.executorService.submit(() -> {
            try
            {
                AtomicReference<Exception> exceptionAtomicReference = new AtomicReference<>();
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                    try
                    {
                        runnableThrowable.run();
                    }
                    catch (Exception e)
                    {
                        exceptionAtomicReference.set(e);
                    }
                    return null;
                });
                if (exceptionAtomicReference.get() != null)
                {
                    throw exceptionAtomicReference.get();
                }
                future.complete(null);
            }
            catch (Exception e)
            {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    ////////////////////////////////////////////////////////////////
    // Getters/Setters
    ////////////////////////////////////////////////////////////////

    @Override
    public BIcon getIcon()
    {
        return ICON;
    }
}
