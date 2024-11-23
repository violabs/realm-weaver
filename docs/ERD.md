
We need to make the DB model flexible enough to manage multiple game types. We will
discover many more attributes as we go along.

We will need to support:
- Stats
- Skills
- Items
- Techniques (Spells, Abilities)
- Abilities
- Additional Mechanics
- Templates for different game types
  - Character Templates
- Game Mechanics
- Character Identity (Class, Race, etc.)

```mermaid
erDiagram
    PLAYER ||--o{ PLAYER_CHARACTER : has
    CHARACTER_ATTRIBUTES }o--o{ PLAYER_CHARACTER : has
    GAME_TEMPLATE ||--o{ CHARACTER_IDENTITY : has
    CHARACTER_IDENTITY ||--o{ CHARACTER_ATTRIBUTES : has
    PLAYER_CHARACTER ||--o{ CHARACTER_IDENTITY : has
    GAME_TEMPLATE |o--o{ GAME : has
    GAME }o--o{ PLAYER_CHARACTER : has
    GAME_TEMPLATE ||--o{ CHARACTER_TEMPLATE : has
    CHARACTER_TEMPLATE ||--o{ CHARACTER_ATTRIBUTES : has
```