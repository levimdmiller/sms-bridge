package ca.levimiller.smsbridge.data.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.BaseModel;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public abstract class AbstractDbIT<T extends BaseModel> {
  protected final Fixture<T> fixture;
  protected final JpaRepository<T, Long> repository;

  /**
   * Creates the abstract db integration test class.
   *
   * @param fixture - creates test objects
   * @param repository - repo to test
   */
  public AbstractDbIT(Fixture<T> fixture,
      JpaRepository<T, Long> repository) {
    this.fixture = fixture;
    this.repository = repository;
  }

  @Test
  void testSaveAndGet() {
    T entity = saveNewEntity();

    T actual = repository.getOne(entity.getId());
    assertEquals(entity, actual);
  }

  @Test
  void testSaveAll_findAll() {
    List<T> entities = Arrays.asList(saveNewEntity(), saveNewEntity());

    List<T> all = repository.findAll();
    assertEquals(all, entities);
  }

  @Test
  void testFindById() {
    T entity = saveNewEntity();
    Optional<T> result = repository.findById(entity.getId());

    assertTrue(result.isPresent());
    assertEquals(entity, result.orElse(null));
  }

  @Test
  void testFindAllById() {
    List<T> entities = Arrays.asList(saveNewEntity(), saveNewEntity());
    List<Long> ids = entities.stream().map(BaseModel::getId).collect(Collectors.toList());

    List<T> all = repository.findAllById(ids);
    assertEquals(all, entities);
  }

  @Test
  void testExistsById() {
    T entity = saveNewEntity();
    assertTrue(repository.existsById(entity.getId()));
  }

  @Test
  void testCount() {
    long current = repository.count();
    saveNewEntity();
    long actual = repository.count();

    assertEquals(actual, current + 1);
  }

  @Test
  void testDeleteById() {
    T entity = saveNewEntity();
    repository.deleteById(entity.getId());

    Optional<T> result = repository.findById(entity.getId());
    assertFalse(result.isPresent());
  }

  @Test
  void testDelete() {
    T entity = saveNewEntity();
    repository.delete(entity);

    Optional<T> result = repository.findById(entity.getId());
    assertFalse(result.isPresent());
  }

  @Test
  void testDeleteAll_Iterable() {
    T notDeleted = saveNewEntity();
    List<T> entities = Arrays.asList(saveNewEntity(), saveNewEntity());
    repository.deleteAll(entities);

    for(T entity : entities) {
      assertFalse(repository.existsById(entity.getId()));
    }
    assertTrue(repository.existsById(notDeleted.getId()));
  }

  @Test
  void testDeleteAll() {
    T entity1 = saveNewEntity();
    T entity2 = saveNewEntity();
    repository.deleteAll();

    assertFalse(repository.existsById(entity1.getId()));
    assertFalse(repository.existsById(entity2.getId()));
  }

  @Test
  void testFindAll_Sort() {
    T entity1 = saveNewEntity();
    T entity2 = saveNewEntity();

    List<T> result = repository.findAll(Sort.by(Direction.DESC, "id"));
    assertEquals(Arrays.asList(entity2, entity1), result);
  }

  @Test
  void testFindAll_Pageable() {
    T entity1 = saveNewEntity();
    T entity2 = saveNewEntity();

    Page<T> first = repository.findAll(PageRequest.of(0, 1));
    assertEquals(2, first.getTotalPages());
    assertEquals(Collections.singletonList(entity1), first.getContent());

    Page<T> second = repository.findAll(first.nextPageable());
    assertEquals(2, second.getTotalPages());
    assertEquals(Collections.singletonList(entity2), second.getContent());
  }

  @Test
  void testfindOneByExample() {
    T entity = saveNewEntity();
    // temp delete id
    Long id = entity.getId();
    
    Optional<T> result = repository.findOne(Example.of(entity));

    assertTrue(result.isPresent());
    entity.setId(id);
    assertEquals(entity, result.orElse(null));
  }

  @Test
  void testfindAllByExample() {
    T entity = saveNewEntity();
    

    List<T> result = repository.findAll(Example.of(entity));

    assertEquals(new HashSet<>(Collections.singletonList(entity)), new HashSet<>(result));
  }

  @Test
  void testfindAllByExample_Sort() {
    T entity = saveNewEntity();
    

    List<T> result = repository.findAll(Example.of(entity), Sort.by(Sort.Direction.DESC, "id"));

    assertEquals(Collections.singletonList(entity), result);
  }

  @Test
  void testfindAllByExample_Pageable() {
    T entity = saveNewEntity();
    

    Page<T> first = repository.findAll(Example.of(entity), PageRequest.of(0, 1));
    assertEquals(1, first.getTotalPages());
    assertEquals(Collections.singletonList(entity), first.getContent());
  }

  @Test
  void testCountByExample() {
    T entity = saveNewEntity();
    
    long result = repository.count(Example.of(entity));
    assertEquals(1, result);
  }

  @Test
  void testExistsByExample() {
    T entity = saveNewEntity();
    
    assertTrue(repository.exists(Example.of(entity)));
  }

  /**
   * Create new entity with fixture, and save it to the repo.
   * @return - saved entity
   */
  protected T saveNewEntity() {
    return repository.save(fixture.create());
  }
}
