package net.corda.core.flows;

import net.corda.core.serialization.AMQPSerializationDefaults;
import net.corda.core.serialization.AMQPSerializationFactory;
import net.corda.core.serialization.SerializationDefaults;
import net.corda.core.serialization.SerializationFactory;
import net.corda.testing.core.AMQPSerializationEnvironmentRule;
import net.corda.testing.core.SerializationEnvironmentRule;
import org.junit.Rule;
import org.junit.Test;

import static net.corda.core.serialization.SerializationAPIKt.serialize;
import static net.corda.core.serialization.AMQPSerializationAPIKt.serialize;
import static org.junit.Assert.assertNull;

/**
 * Enforce parts of the serialization API that aren't obvious from looking at the {@link net.corda.core.serialization.SerializationAPIKt} code.
 */
public class SerializationApiInJavaTest {

    @Rule
    public final SerializationEnvironmentRule testSerialization = new SerializationEnvironmentRule();

    @Rule
    public final AMQPSerializationEnvironmentRule testAMQPSerialization = new AMQPSerializationEnvironmentRule();

    @Test
    public void enforceSerializationFactoryApi() {
        assertNull(SerializationFactory.Companion.getCurrentFactory());
        SerializationFactory factory = SerializationFactory.Companion.getDefaultFactory();
        assertNull(factory.getCurrentContext());
        serialize("hello", factory, factory.getDefaultContext());
    }

    @Test
    public void enforceSerializationDefaultsApi() {
        SerializationDefaults defaults = SerializationDefaults.INSTANCE;
        SerializationFactory factory = defaults.getSERIALIZATION_FACTORY();
        serialize("hello", factory, defaults.getCHECKPOINT_CONTEXT());

        AMQPSerializationDefaults amqpDefaults = AMQPSerializationDefaults.INSTANCE;
        AMQPSerializationFactory amqpFactory = amqpDefaults.getSERIALIZATION_FACTORY();
        serialize("hello", amqpFactory, amqpDefaults.getP2P_CONTEXT());
        serialize("hello", amqpFactory, amqpDefaults.getRPC_SERVER_CONTEXT());
        serialize("hello", amqpFactory, amqpDefaults.getRPC_CLIENT_CONTEXT());
        serialize("hello", amqpFactory, amqpDefaults.getSTORAGE_CONTEXT());
    }
}
