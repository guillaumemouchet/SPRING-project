package ch.hearc.jee2022.catalog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import ch.hearc.jee2022.catalog.model.Utilisateur;

public interface UtilisateurRepository
		extends CrudRepository<Utilisateur, Long>, PagingAndSortingRepository<Utilisateur, Long> {

	Utilisateur findByName(String name);

}