package ch.hearc.jee2022.catalog.repository;

import org.springframework.data.repository.CrudRepository;

import ch.hearc.jee2022.catalog.model.Utilisateur;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {

	Utilisateur findByName(String name);
}