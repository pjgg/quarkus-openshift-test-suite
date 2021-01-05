package io.quarkus.ts.openshift.messaging.kafka.containers;

import io.strimzi.StrimziKafkaContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;

public class SchemaRegistryContainer extends GenericContainer<SchemaRegistryContainer> {

    private final int port;

    public SchemaRegistryContainer(final String image, final String version, final int port) {
        super(image + ":"+ version);
        this.port = port;
        withExposedPorts(port);
    }

    public SchemaRegistryContainer withKafka(StrimziKafkaContainer kafka, int kPort) {
        return withStrimziKafka(kafka.getNetwork(), kafka.getNetworkAliases().get(0) + ":" + kPort);
    }

    protected SchemaRegistryContainer withStrimziKafka(Network network,String bootstrapServers) {
        withNetwork(network);
        withEnv("QUARKUS_PROFILE", "strimzi");
        withEnv("APPLICATION_ID", "registry_id");
        withEnv("KAFKA_BOOTSTRAP_SERVERS", bootstrapServers);
        withEnv("APPLICATION_SERVER", "localhost:9000");
        return self();
    }

    /**
     * @return Schema Registry URL
     */
    public String getSchemaRegistryUrl() {
        return "http://" + getContainerIpAddress() + ":" + getMappedPort(port);
    }
}
