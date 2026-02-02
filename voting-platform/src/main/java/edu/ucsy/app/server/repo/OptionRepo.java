package edu.ucsy.app.server.repo;

import edu.ucsy.app.server.entities.Option;
import edu.ucsy.app.server.entities.pk.OptionPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepo extends JpaRepository<Option, OptionPk> {
}
