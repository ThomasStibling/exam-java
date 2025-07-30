-- Données de test pour l'application de gestion de tickets

-- Insertion des priorités
INSERT INTO priorities (nom) VALUES 
('Basse'),
('Normale'),
('Haute'),
('Critique');

-- Insertion des catégories
INSERT INTO categories (nom) VALUES 
('Bug'),
('Amélioration'),
('Demande'),
('Support'),
('Sécurité');

-- Insertion des utilisateurs (mots de passe hashés avec BCrypt)
-- Mot de passe: "password" pour tous les utilisateurs
INSERT INTO users (pseudo, password, admin) VALUES 
('admin', '$2a$12$Dhgo3Wgd6xj6CsbOYbrCOePykB4Tf.ZYB/.yrYRy21j8hZktg5Fou', true),
('user1', '$2a$12$Dhgo3Wgd6xj6CsbOYbrCOePykB4Tf.ZYB/.yrYRy21j8hZktg5Fou', false),
('user2', '$2a$12$Dhgo3Wgd6xj6CsbOYbrCOePykB4Tf.ZYB/.yrYRy21j8hZktg5Fou', false),
('support', '$2a$12$Dhgo3Wgd6xj6CsbOYbrCOePykB4Tf.ZYB/.yrYRy21j8hZktg5Fou', true);

-- Insertion des tickets
INSERT INTO tickets (titre, description, resolu, submitter_id, priority_id) VALUES 
('Problème de connexion', 'Impossible de se connecter à l''application', false, 2, 3),
('Amélioration de l''interface', 'Ajouter un mode sombre à l''interface', false, 3, 2),
('Bug dans le formulaire', 'Le formulaire ne valide pas les champs requis', true, 2, 4),
('Demande de fonctionnalité', 'Ajouter la possibilité d''exporter les données', false, 3, 2),
('Problème de performance', 'L''application est lente lors du chargement', false, 2, 3);

-- Association des tickets avec des catégories
INSERT INTO ticket_categories (ticket_id, category_id) VALUES 
(1, 1), -- Problème de connexion -> Bug
(1, 4), -- Problème de connexion -> Support
(2, 2), -- Amélioration de l'interface -> Amélioration
(3, 1), -- Bug dans le formulaire -> Bug
(4, 3), -- Demande de fonctionnalité -> Demande
(5, 1), -- Problème de performance -> Bug
(5, 4); -- Problème de performance -> Support 