package de.keyruu.nexcalimat.repository;

import de.keyruu.nexcalimat.model.Favorite;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FavoriteRepository implements PanacheRepository<Favorite>
{
  //
}
