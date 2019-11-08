package fr.btib.firebase.connector;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import fr.btib.connector.exception.ConnectorDisabledException;
import fr.btib.connector.interfaces.RunnableThrowable;
import fr.btib.connector.realtime.BRealtimeConnector;
import fr.btib.connector.realtime.interfaces.BIIncomingMessageListener;
import fr.btib.connector.realtime.interfaces.BIRealtimeDeviceExtension;
import fr.btib.connector.realtime.interfaces.BIRealtimePointExtension;
import fr.btib.connector.realtime.messages.outgoing.IOutgoingPointMessage;
import fr.btib.core.BtibLogger;
import fr.btib.core.tool.BtibIconTool;
import fr.btib.core.tool.CompTool;

import javax.baja.nre.annotations.NiagaraProperty;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.*;
import java.io.ByteArrayInputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
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
        return getString(firebaseKeyJson);
    }

    /**
     * Set the {@code firebaseKeyJson} property.
     *
     * @see #firebaseKeyJson
     */
    public void setFirebaseKeyJson(String v)
    {
        setString(firebaseKeyJson, v, null);
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

    private static final BtibLogger LOG = BtibLogger.getLogger(TYPE);
    private static final BIcon ICON = BtibIconTool.getComponentIcon(TYPE);
    private static final String DATABASE_URL = "https://realtimeconnectordemo.firebaseio.com";


    private FirebaseApp firebaseApp = null;
    private Firestore firestore = null;
    private final ExecutorService executorService = AccessController.doPrivileged((PrivilegedAction<ExecutorService>) Executors::newCachedThreadPool);

    @Override
    public void doPing()
    {
        setLastAttempt(BAbsTime.now());
        pingService()
            .exceptionally(e -> {
                CompTool.setFault(this, e.getMessage(), e, LOG);
                setLastFailure(BAbsTime.now());
                return null;
            })
            .thenRun(() -> {
                CompTool.setOk(this);
                setLastSuccess(BAbsTime.now());
            });
    }

    private void setLastAttempt()
    {
    }

    @Override
    public Class<? extends BIRealtimeDeviceExtension> getDeviceExtensionClass()
    {
        return null;
    }

    @Override
    public Class<? extends BIRealtimePointExtension> getPointExtensionClass()
    {
        return null;
    }

    @Override
    public CompletableFuture<Void> registerDevice(String s)
    {
        return null;
    }

    @Override
    public CompletableFuture<Void> registerPoint(String s, String s1)
    {
        return null;
    }

    @Override
    public CompletableFuture<Void> unregisterDevice(String s)
    {
        return null;
    }

    @Override
    public CompletableFuture<Void> unregisterPoint(String s, String s1)
    {
        return null;
    }

    @Override
    public CompletableFuture<Void> openConnectionForDevice(String s)
    {
        return null;
    }

    @Override
    public CompletableFuture<Void> closeConnectionForDevice(String s)
    {
        return null;
    }

    @Override
    public boolean isDeviceConnected(String s) throws Exception
    {
        return false;
    }

    @Override
    public CompletableFuture<Void> sendMessage(String s, IOutgoingPointMessage iOutgoingPointMessage)
    {
        return null;
    }

    @Override
    public CompletableFuture<Void> sendTagsForDevice(String s, Map<String, String> map)
    {
        return null;
    }

    @Override
    public CompletableFuture<Void> sendTagsForPoint(String s, String s1, Map<String, String> map)
    {
        return null;
    }

    @Override
    public void subscribeToIncomingMessages(String s, BIIncomingMessageListener biIncomingMessageListener) throws Exception
    {

    }

    @Override
    public void unsubscribeToIncomingMessages(String s, BIIncomingMessageListener biIncomingMessageListener) throws Exception
    {

    }

    @Override
    public CompletableFuture<Void> pingService()
    {
        return doAsync(() -> {
            firestore = getFirestore();
            Iterable<CollectionReference> collections = firestore.getCollections();
            collections.forEach(collectionReference -> System.out.println(collectionReference.getId()));
        });
    }

    @Override
    public BtibLogger getBtibLogger()
    {
        return null;
    }

    @Override
    public void started() throws Exception
    {
        super.started();
        // check if the station is already started when you drag and drop the component for the first time
        if (!Sys.isStationStarted())
        {
            // When station is booting
            firebaseApp = initializeFirebase();
        }
    }

    private Firestore getFirestore() throws Exception
    {
        if (firestore == null)
        {
            if (firebaseApp == null)
            {
                firebaseApp = initializeFirebase();
            }
            firestore = AccessController.doPrivileged((PrivilegedAction<Firestore>) () -> {
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
        }
        return firestore;
    }

    private FirebaseApp initializeFirebase() throws Exception

    {
        AtomicReference<Exception> innerException = new AtomicReference<>();
        FirebaseApp app = AccessController.doPrivileged((PrivilegedAction<FirebaseApp>) () -> {
            try
            {
                ByteArrayInputStream credentialsStream = new ByteArrayInputStream(getFirebaseKeyJson().getBytes());
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

    private CompletableFuture<Void> doAsync(RunnableThrowable runnableThrowable)
    {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (!getEnabled())
        {
            future.completeExceptionally(new ConnectorDisabledException());
        }
        executorService.submit(() -> {
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
