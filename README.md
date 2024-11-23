# Realm Weaver
A system for building tabletop RPGs

> **Note:** This project is in the early stages of development and is not yet ready for use.
>           This is also to write out test functionality for different TestContainers.

## ERD

Basic ERD for the project can be found at [ERD](./docs/ERD.md). Updated on 2024-11-23.

## Roadmap

Overall monolithic structure with a focus on modularity and scalability.
We can break it into microservices later if needed. With this in mind,
we can create modules for different aspects of the game.

- [x] ERD (initial)
- [ ] Player Functionality
  - [ ] Build out entity access
  - [ ] Build out functional tests
- [ ] Character Functionality
    - [ ] Build out basic character functionality
- [ ] Turn Based Functionality (Combat, Social, etc.)