package com.example.oreily;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}
}

@Getter
@Setter
class Coffee {
	private final String id;
	private String name;

	public Coffee(String id, String name) {
		this.id = id;
		this.name = name;
	}

	// принимает уникальный идентификатор,
	// если он не указан при создании экземпляра Coffee
	public Coffee(String name) {
		this(UUID.randomUUID().toString(), name);
	}
}

@RestController
@RequestMapping("/coffees")
class RestApiDemoController {
	private List<Coffee> coffees = new ArrayList<>();

	public RestApiDemoController() {
		coffees.addAll(List.of(
				new Coffee("Cafe 1" ),
				new Coffee("Cafe 2" ),
				new Coffee("Cafe 3" ),
				new Coffee("Cafe 4" )
		));
	}

	// Этот метод отвечает за извлечение данных,
	// но не за обновления какого-либо вида.
//	@RequestMapping(value = "/coffee", method = RequestMethod.GET)
//	Iterable<Coffee> getCoffees() {
//		return coffees;
//	}
	@GetMapping
	Iterable<Coffee> getCoffees() {
		return coffees;
	}

	@GetMapping("/{id}")
	Optional<Coffee> getCoffeeById(@PathVariable String id) {
		for (Coffee c: coffees) {
			if (c.getId().equals(id)) {
				return Optional.of(c);
			}
		}
		return Optional.empty();
	}

	@PostMapping
	Coffee postCoffee(@RequestBody Coffee coffee) {
		coffees.add(coffee);
		return coffee;
	}

	// Следующий код работает в полном соответствии со спецификацией: найти вид
	// кофе с указанным идентификатором, если найден — обновить. Если в списке
	// нет такого вида кофе — создает его:

	// Теперь метод putCoffee будет возвращать не только модифицированный или созданный
	// объект Coffee , но и объект ResponseEntity , содержащий вышеупомянутый объект
	// Coffee и код состояния HTTP: 201 (Создано) , если соответствующий вид кофе
	// отсутствовал в списке, и 200 (OK) , если он уже существовал и был обновлен.
	@PutMapping("/{id}")
	ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
		int coffeeIndex = -1;
		for (Coffee c: coffees) {
			if (c.getId().equals(id)) {
				coffeeIndex = coffees.indexOf(c);
				coffees.set(coffeeIndex, coffee);
			}
		}
		return (coffeeIndex == -1) ?
				new ResponseEntity<>(postCoffee(coffee), HttpStatus.CREATED) :
				new ResponseEntity<>(postCoffee(coffee), HttpStatus.OK);
	}

	// Для удаления ресурсов используются запросы HTTP DELETE . Как показано в сле-
	// дующем фрагменте кода, мы создали метод, принимающий идентификатор вида
	// кофе в переменной @PathVariable и удаляющий соответствующий вид из нашего
	// списка с помощью метода removeIf интерфейса Collection 1 . Метод removeIf
	// получает предикат Predicate , так что можно передать лямбда-выражение, воз-
	// вращающее True для вида кофе, который нужно удалить. Просто и эффективно:
	@DeleteMapping("/{id}")
	void deleteCoffee(@PathVariable String id) {
		coffees.removeIf(c -> c.getId().equals(id));
	}

}

