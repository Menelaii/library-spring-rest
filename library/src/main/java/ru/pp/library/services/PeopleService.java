package ru.pp.library.services;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.pp.library.entities.Person;
import ru.pp.library.exceptions.NotFoundException;
import ru.pp.library.repositories.PeopleRepository;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    @Value("${books.expiredAfter}")
    private long expiredAfter;
    private PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            person.get().getBooks().forEach(
                    book -> {
                        long millis = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                        if(millis > expiredAfter) {
                            book.setExpired(true);
                        }
                    });
        }

        return person.orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(int id) {
        throwNotFoundIfNotExist(id);
        peopleRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Person person) {
        throwNotFoundIfNotExist(id);
        person.setId(id);
        peopleRepository.save(person);
    }

    private void throwNotFoundIfNotExist(int id) {
        if(!peopleRepository.existsById(id)) {
            throw new NotFoundException();
        }
    }
}