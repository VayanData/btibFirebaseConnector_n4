package fr.btib.firebase.connector;

import com.google.auth.oauth2.GoogleCredentials;
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
import fr.btib.connector.realtime.messages.incoming.factory.IncomingMessageFactory;
import fr.btib.connector.realtime.messages.outgoing.IOutgoingPointMessage;
import fr.btib.connector.realtime.messages.outgoing.StatusPointMessage;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;


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
    private static final String POINTS_COLLECTION = "points";
    private static final String MESSAGES_COLLECTION = "messages";
    // the workers thread pool
    private final ExecutorService executorService = AccessController.doPrivileged((PrivilegedAction<ExecutorService>) Executors::newCachedThreadPool);
    // Devices incoming messages listeners
    private final Map<String, BIIncomingMessageListener> listeners = new HashMap<>();
    private FirebaseApp firebaseApp = null;
    private Firestore firestore = null;
    private ListenerRegistration messageListenerRegistration;

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
        return this.doAsync(() -> this.getFirestore().collection(DEVICES_COLLECTION).document(deviceId).create(new HashMap<>()).get());
    }

    @Override
    public CompletableFuture<Void> registerPoint(String deviceId, String pointId)
    {
        return this.doAsync(() -> this.getFirestore().collection(DEVICES_COLLECTION).document(deviceId).collection(POINTS_COLLECTION).document(pointId).create(new HashMap<>()).get());
    }

    @Override
    public CompletableFuture<Void> unregisterDevice(String deviceId)
    {
        return this.doAsync(() -> this.getFirestore().collection(DEVICES_COLLECTION).document(deviceId).delete().get());
    }

    @Override
    public CompletableFuture<Void> unregisterPoint(String deviceId, String pointId)
    {
        return this.doAsync(() -> this.getFirestore().collection(DEVICES_COLLECTION).document(deviceId).collection(POINTS_COLLECTION).document(pointId).delete().get());
    }

    @Override
    public CompletableFuture<Void> openConnectionForDevice(String deviceId)
    {
        return this.doAsync(this::getFirestore);
    }

    @Override
    public CompletableFuture<Void> closeConnectionForDevice(String s)
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
    public CompletableFuture<Void> sendMessage(String deviceId, IOutgoingPointMessage message)
    {
        return this.doAsync(() -> {
            if (message instanceof StatusPointMessage)
            {
                StatusPointMessage statusPointMessage = (StatusPointMessage) message;
                HashMap<String, Object> fields = new HashMap<>();
                fields.put(StatusPointMessage.STATUS, statusPointMessage.getStatus());
                fields.put(StatusPointMessage.VALUE, statusPointMessage.getValue());
                fields.put(StatusPointMessage.TIMESTAMP, statusPointMessage.getTimestamp());
                this.getFirestore().collection(DEVICES_COLLECTION).document(deviceId).collection(POINTS_COLLECTION).document(statusPointMessage.getPointId()).set(fields).get();
            }
            else
            {
                throw new Exception("Unsupported message type: " + message.getClass().getName());
            }
        });
    }

    @Override
    public CompletableFuture<Void> sendTagsForDevice(String deviceId, Map<String, String> tags)
    {
        return this.doAsync(() -> {
            Map<String, Object> fields = new HashMap<>();
            fields.put("tags", tags);
            this.getFirestore().collection(DEVICES_COLLECTION).document(deviceId).set(fields).get();
        });
    }

    @Override
    public CompletableFuture<Void> sendTagsForPoint(String deviceId, String pointId, Map<String, String> tags)
    {
        return this.doAsync(() -> {
            Map<String, Object> fields = new HashMap<>();
            fields.put("tags", tags);
            this.getFirestore().collection(DEVICES_COLLECTION).document(deviceId).collection(POINTS_COLLECTION).document(pointId).set(fields).get();
        });
    }

    @Override
    public void subscribeToIncomingMessages(String deviceId, BIIncomingMessageListener incomingMessageListener) throws Exception
    {
        this.listeners.put(deviceId, incomingMessageListener);
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

    /**
     * Create the firestore lazily
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
     * Start limning for messages
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
                            BFirebaseConnector.this.listeners.values().forEach(listener -> {
                                try
                                {
                                    listener.handleIncomingMessage(IncomingMessageFactory.create(jsonMessage));
                                }
                                catch (Exception e)
                                {
                                    LOG.severe(e.getMessage());
                                }
                            });
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

    @Override
    public BIcon getIcon()
    {
        return ICON;
    }
}
