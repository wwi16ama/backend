# Styleguide

## Classes

1. `class` or interface statement
2. Class/interface implementation
3. Class (`static`) variables 
    - First the public class variables, then the protected, and then the private.
4. Instance variables 
    - First public, then protected, and then private.
5. Constructors
6. Methods 
    - These methods should be grouped by functionality rather than by scope or accessibility. For example, a private class method can be in between two public instance methods. The goal is to make reading and understanding the code easier.

## Exceptions   

Lieber keine neue Exception schreiben, es gibt wahrscheinlich schon irgenwas, was zutrifft.

## REST

Die HTTP-Methoden und Statuscodes sollten für sich sprechen können

## SpringBoot

## IntelliJ

grauen Balken rechts beachten

### RestController

- Short might be elegant, i.e.
    - `@GetMapping` instead of `@RequestMapping(method = RequestMethod.GET)`
- Nicht unnötig explizit werden.
    - lieber `return member;` statt `return new ResponseEntity<Member>(member, HttpStatus.SUCCESS);`
- Reihenfolge der Handler-Methoden:
    - allgemein vor spezifisch
        - `/member` vor `/member/{id}`
    - Reihenfolge für Methoden: zuerst `GET`, dann `POST`, danach `PUT`, zum schluss `DELETE`. Wie im Swagger Editor.