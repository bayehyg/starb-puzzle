
package starb.server.repo;
import org.springframework.data.mongodb.repository.MongoRepository;
import starb.client.Client;

public interface ClientRepository extends MongoRepository<Client, String> {
    Client findClientById(String id);
    Client ip(String ip);
}
