# StackUnderflow
## Par Alexandre Piveteau, Guy-Laurent Subri, Matthieu Burguburu, David Dupraz, du café

## Points forts
+ Intégration continue
+ Tests
+ Redirection
+ Stabilité, conception

## Intégration continue
+ Tout est automatisé, jamais de déploiement à la main
+ Déploiement en containers Docker et sur Heroku automatiquement
+ Pas de configuration à modifier entre les deux
+ Tests effectués à chaque merge puis déploiement, sur les 2 plateformes

## Tests
+ Tests très complets
+ Différentes technologies (Arquilian, Codecept)
+ Couvrent tout le spectre du code, à tous les stades
+ Code coverage très bonne - > code très fiable

## Redirection
+ Opérations impossibles sans login ramènent vers login
+ Autres redirections du genre
+ Page 404 personnalisée (thx to aliens)
+ Améliore l'expérience utilisateur

## Stabilité, conception
+ Code réparti en packages selon modèle MVC
+ Façades complètes et fonctionnelles
+ Classes de commande, de DTO, de requêtes...
+ Code très "pro" -> très fiable