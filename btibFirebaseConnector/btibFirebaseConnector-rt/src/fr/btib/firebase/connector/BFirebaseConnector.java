package fr.btib.firebase.connector;

import com.google.auth.oauth2.GoogleCredentials;
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
import fr.btib.core.tool.CompTool;

import javax.baja.nre.annotations.NiagaraProperty;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.BAbsTime;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@NiagaraType
@NiagaraProperty(
    name = "firebaseKeyJson",
    type = "String",
    defaultValue = ""
)
public class BFirebaseConnector extends BRealtimeConnector
{
    /*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
    /*@ $com.btib.btibFirebaseConnector.BFirebaseConnector(157606577)1.0$ @*/
    /* Generated Thu Nov 07 16:48:09 CET 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

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

    private FirebaseApp firebaseApp = null;
    private Firestore firestore = null;
    private ExecutorService executorService = AccessController.doPrivileged((PrivilegedAction<ExecutorService>) Executors::newCachedThreadPool);

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
        return this.doAsync(() -> this.getFirestore().listCollections().forEach(System.out::println));
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
            this.firebaseApp = this.initializeFirebase();
        }
    }

    private Firestore getFirestore() throws IOException
    {
        if (this.firestore == null)
        {
            if (this.firebaseApp == null)
            {
                this.firebaseApp = this.initializeFirebase();
            }
            this.firestore = FirestoreClient.getFirestore();
        }
        return this.firestore;
    }

    private FirebaseApp initializeFirebase() throws IOException
    {
        ByteArrayInputStream credentialsStream = new ByteArrayInputStream(getFirebaseKeyJson().getBytes());
        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(credentialsStream))
            .build();
        return FirebaseApp.initializeApp(options);
    }

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
                runnableThrowable.run();
                future.complete(null);
            }
            catch (Exception e)
            {
                future.completeExceptionally(e);
            }
        });
        return future;
    }
}
