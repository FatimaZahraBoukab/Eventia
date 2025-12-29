-- ============================================
-- DONNÉES INITIALES - EVENTIA
-- Système de Gestion de Réservations d'Événements
-- ============================================

-- ============================================
-- UTILISATEURS
-- ============================================
-- Colonnes: enabled, created_at, id, email, full_name, password, role

INSERT INTO users (id, enabled, created_at, email, full_name, password, role) VALUES
(2, TRUE, TIMESTAMP '2025-12-05 18:23:03.158029', 'admin@eventia.com', 'Admin Principal', '$2a$10$8Hat/sDu5N3wjT7NbSZpEubxMz0dVugfrKNJ6X78v8dJNUHc8DznG', 'ADMIN'),
(3, TRUE, TIMESTAMP '2025-12-05 18:24:51.156121', 'marie@eventia.com', 'Marie Organisatrice1', '$2a$10$RLYPLp1a/pJ1TJVJ3Dh4SumOilsDQFdpqJBq0Y4uA2DntT51m4dRC', 'ORGANIZER'),
(4, TRUE, TIMESTAMP '2025-12-05 21:22:39.419006', 'fatimazahraboukab9@gmail.com', 'fatimazahraboukab', '$2a$10$X04TD.YQgUtqDFYGeEdINepMqhDbhikqCDcO4mdRVhI/kpCUQll0e', 'CLIENT'),
(5, FALSE, TIMESTAMP '2025-12-05 21:37:44.647816', 'Aatar@gmail.com', 'karima Aatar', '$2a$10$TZniW6SfcRjHGqHXbWWNheDjNfHB5I0h1Q2Kplv0kp4SIzInsYrlS', 'CLIENT'),
(7, TRUE, TIMESTAMP '2025-12-12 13:48:52.913676', 'FatimaRafiai@gmail.com', 'Fatima Rafiai', '$2a$10$vSxCeI1tFbG/KW7iXKTjoOAzWQe5OHJsDjXmltPYo7bz.ikFA.LLW', 'CLIENT'),
(8, TRUE, TIMESTAMP '2025-12-12 13:49:32.66161', 'Maroua@eventia.com', 'Maroua Organnisatrice', '$2a$10$IBl0FLuU9v/WA53VpNDV1Oz2jk9y39RvWTla/HqtuwB6ZWqUF4tPW', 'ORGANIZER'),
(39, TRUE, TIMESTAMP '2025-12-18 21:25:31.44103', 'mohamed@eventia.com', 'mohamed boukab', '$2a$10$NQWZTgtdP2xsRjgjqpn5fuL.JzOKGkHFO4Q8pyeQqoH3Gt/KkjrBa', 'ORGANIZER'),
(40, TRUE, TIMESTAMP '2025-12-18 21:26:30.027749', 'hajarboukab@gmail.com', 'Hajar boukab', '$2a$10$zxq/1x1lUZFymQHjGLMDo.7SV/TqUJaITwBiaxGDupPPOzZdvkUdm', 'CLIENT'),
(41, TRUE, TIMESTAMP '2025-12-19 22:12:52.377789', 'kaoutarelfatmi@gmail.com', 'kaoutar el fatmi', '$2a$10$w9SwOgHgEkJzS1n.htdvFO3dMGtG/hKChgdaHMc/wOEgggs2QddQW', 'CLIENT');


-- ============================================
-- ÉVÉNEMENTS
-- ============================================
-- Colonnes: id, capacite_max, categorie, date_creation, date_debut, date_fin, date_modification, description, image_path, lieu, organisateur_id, prix_unitaire, statut, titre, ville

INSERT INTO events (id, capacite_max, categorie, date_creation, date_debut, date_fin, date_modification, description, image_path, lieu, organisateur_id, prix_unitaire, statut, titre, ville) VALUES
(45, 250, 'CONCERT', TIMESTAMP '2025-12-14 16:45:56.067755', TIMESTAMP '2024-12-13 19:00:00', TIMESTAMP '2025-12-16 22:00:00', TIMESTAMP '2025-12-14 22:47:06.925597', 'Une soirée de gala prestigieuse réunissant des invités de marque dans un cadre luxueux. L''événement propose un dîner gastronomique, une ambiance musicale raffinée et des animations élégantes. Idéal pour les amateurs de soirées haut de gamme et de networking. Une expérience unique alliant élégance, convivialité et excellence du service.', 'https://i.pinimg.com/736x/f3/f4/2e/f3f42e5c1d6a4151e968c929fb2450bc.jpg', 'Hôtel Royal Mansour', 3, 600.0, 'TERMINE', 'Soirée Gala Prestige', 'Marrakech'),
(77, 900, 'CONCERT', TIMESTAMP '2025-12-14 17:52:41.9344', TIMESTAMP '2026-01-03 20:00:00', TIMESTAMP '2026-01-03 23:00:00', TIMESTAMP '2025-12-14 18:18:02.682418', 'Un concert live réunissant des artistes pop et jazz reconnus sur la scène nationale. Le public profitera d''une ambiance chaleureuse et d''une acoustique exceptionnelle. Une soirée musicale parfaite pour se détendre et découvrir de nouveaux talents. Un rendez-vous incontournable pour les passionnés de musique live.', 'https://i.pinimg.com/1200x/21/0f/0e/210f0ef72578c091278659c49e46cd66.jpg', 'Théâtre Mohammed V', 3, 180.0, 'PUBLIE', 'Concert Live Pop & Jazz', 'Rabat'),
(78, 500, 'CONFERENCE', TIMESTAMP '2025-12-14 18:02:37.298914', TIMESTAMP '2026-01-10 09:00:00', TIMESTAMP '2026-01-10 17:00:00', TIMESTAMP '2025-12-14 18:21:20.814394', 'Une conférence dédiée aux nouvelles tendances en innovation et technologie. Des experts partageront leurs expériences à travers des conférences et des ateliers pratiques. Les participants auront l''occasion d''échanger et de développer leur réseau professionnel. Un événement enrichissant pour étudiants, professionnels et entrepreneurs.', 'https://i.pinimg.com/1200x/2e/e9/1d/2ee91d2482aad3cabe1e2f51a5a36119.jpg', 'Technopark', 3, 200.0, 'ANNULE', 'Conférence Innovation & Technologie', 'Casablanca'),
(80, 30, 'CONCERT', TIMESTAMP '2025-12-14 18:06:56.25685', TIMESTAMP '2026-01-17 21:00:00', TIMESTAMP '2026-01-18 02:00:00', TIMESTAMP '2025-12-14 18:34:29.258574', 'Une soirée DJ animée avec des sons modernes et une ambiance festive. Les meilleurs DJs assureront une atmosphère dynamique tout au long de la nuit. L''événement est idéal pour les amateurs de musique électronique et de danse. Une nuit inoubliable dans un cadre moderne et sécurisé.', 'https://i.pinimg.com/736x/8a/72/78/8a72780991b0aa7fd6e5e75a95c36ab5.jpg', 'Sky Lounge', 3, 250.0, 'PUBLIE', 'Soirée DJ', 'Agadir'),
(81, 150, 'CONFERENCE', TIMESTAMP '2025-12-14 18:17:52.476803', TIMESTAMP '2026-02-07 10:00:00', TIMESTAMP '2026-02-07 16:00:00', TIMESTAMP '2025-12-14 18:34:47.202623', 'Un séminaire axé sur le développement personnel et la motivation. Des coachs spécialisés proposeront des ateliers interactifs et inspirants. Les participants apprendront à mieux gérer leur temps et leurs objectifs. Une journée enrichissante pour améliorer son bien-être personnel et professionnel.', 'https://i.pinimg.com/1200x/80/0a/18/800a1887b70ea8fb0749500e61446b20.jpg', 'Centre Culturel', 3, 300.0, 'PUBLIE', 'Séminaire Développement Personnel', 'Fès'),
(82, 700, 'AUTRE', TIMESTAMP '2025-12-14 18:20:53.73739', TIMESTAMP '2026-01-15 10:00:00', TIMESTAMP '2026-01-15 18:00:00', TIMESTAMP '2025-12-14 18:21:28.200844', 'Une exposition mettant en avant des œuvres d''art et de design contemporain. Les visiteurs découvriront des créations originales d''artistes locaux et internationaux. L''événement offre un espace d''échange et de découverte culturelle. Une expérience artistique enrichissante accessible à tous les publics.', 'https://i.pinimg.com/736x/d1/ea/46/d1ea4630890efc5305d81969e5995b27.jpg', 'Galerie d''Art Moderne', 3, 150.0, 'PUBLIE', 'Exposition Art & Design', 'Rabat'),
(84, 120, 'AUTRE', TIMESTAMP '2025-12-14 18:28:28.433745', TIMESTAMP '2026-02-18 18:00:00', TIMESTAMP '2026-02-18 22:00:00', TIMESTAMP '2025-12-14 18:28:28.433745', 'Une soirée dédiée aux rencontres professionnelles et aux échanges d''idées. L''événement réunit entrepreneurs, étudiants et experts de différents domaines. Les participants pourront élargir leur réseau et découvrir de nouvelles opportunités. Un cadre convivial favorisant la collaboration et le partage d''expériences.', 'https://i.pinimg.com/1200x/14/58/e9/1458e9d52944d275b275e0f2adf6e0db.jpg', 'Coworking Space Central', 3, 150.0, 'BROUILLON', 'Soirée Networking Professionnel', 'Casablanca'),
(85, 450, 'THEATRE', TIMESTAMP '2025-12-14 18:33:42.835186', TIMESTAMP '2026-01-03 18:00:00', TIMESTAMP '2026-01-03 20:00:00', TIMESTAMP '2025-12-14 18:33:50.827399', 'Une pièce dramatique traitant de thèmes sociaux profonds et actuels. Le scénario met en lumière des histoires humaines touchantes et réalistes. Les performances des acteurs offrent une intensité émotionnelle remarquable. Un spectacle engagé qui invite à la réflexion et au dialogue.', 'https://i.pinimg.com/1200x/12/ba/61/12ba618c78fcfc3c8d42052523b249ab.jpg', 'Centre Culturel', 3, 300.0, 'PUBLIE', 'Drame Théâtral : Silences Brisés', 'Fès'),
(86, 2000, 'SPORT', TIMESTAMP '2025-12-14 18:43:03.873453', TIMESTAMP '2026-01-15 10:00:00', TIMESTAMP '2026-01-15 18:00:00', TIMESTAMP '2025-12-14 18:43:11.970047', 'Un tournoi de basketball réunissant plusieurs équipes locales et régionales. Les matchs se dérouleront dans une ambiance compétitive et conviviale. Les spectateurs pourront profiter de rencontres intenses et spectaculaires. Un rendez-vous sportif dynamique pour toute la famille.', 'https://i.pinimg.com/736x/43/56/ac/4356acdff8ac7c061df6e8034ae26300.jpg', 'Complexe Sportif Couvert', 3, 100.0, 'PUBLIE', 'Tournoi de Basketball Indoor', 'Meknès'),
(87, 250, 'AUTRE', TIMESTAMP '2025-12-14 18:45:07.495351', TIMESTAMP '2026-02-07 21:00:00', TIMESTAMP '2026-02-07 23:00:00', TIMESTAMP '2025-12-14 18:45:14.899592', 'Une soirée de gala prestigieuse réunissant des invités de marque dans un cadre luxueux. L''événement propose un dîner gastronomique, une ambiance musicale raffinée et des animations élégantes. Idéal pour les amateurs de soirées haut de gamme et de networking. Une expérience unique alliant élégance, convivialité et excellence du service.', 'https://i.pinimg.com/1200x/dc/cc/15/dccc155175a5523e83933c543a5fb060.jpg', 'Hôtel Royal Mansour', 3, 600.0, 'PUBLIE', 'Soirée Gala Prestige', 'Marrakech'),
(88, 500, 'CONFERENCE', TIMESTAMP '2025-12-14 18:48:41.287202', TIMESTAMP '2026-01-21 10:00:00', TIMESTAMP '2026-01-21 17:00:00', TIMESTAMP '2025-12-14 18:48:47.091325', 'Une conférence dédiée aux nouvelles tendances en innovation et technologie. Des experts partageront leurs expériences à travers des conférences et des ateliers pratiques. Les participants auront l''occasion d''échanger et de développer leur réseau professionnel. Un événement enrichissant pour étudiants, professionnels et entrepreneurs.', 'https://i.pinimg.com/1200x/a0/2b/06/a02b06c838b9f083d5e0d95c4b191880.jpg', 'Technopark', 3, 200.0, 'PUBLIE', 'Conférence Innovation', 'Tanger'),
(89, 5000, 'SPORT', TIMESTAMP '2025-12-14 18:51:15.914656', TIMESTAMP '2026-02-28 07:00:00', TIMESTAMP '2026-03-28 11:00:00', TIMESTAMP '2025-12-14 18:51:25.097304', 'Une course urbaine ouverte aux amateurs et aux sportifs confirmés. Le parcours traverse les principaux quartiers et sites emblématiques de la ville. L''événement encourage le sport, la santé et l''esprit de compétition. Une expérience sportive motivante dans une ambiance festive.', 'https://i.pinimg.com/736x/9f/ac/b4/9facb40f5bab9c2d7aaf4b9561185e12.jpg', 'Centre-ville', 3, 80.0, 'PUBLIE', 'Semi-Marathon Urbain', 'Rabat'),
(109, 400, 'THEATRE', TIMESTAMP '2025-12-14 20:32:29.424034', TIMESTAMP '2026-01-17 20:00:00', TIMESTAMP '2026-01-17 22:00:00', TIMESTAMP '2025-12-14 20:32:45.663696', 'Une comédie légère et pleine d''humour qui plonge le public dans les coulisses du théâtre. Les situations comiques et les dialogues dynamiques garantissent des fous rires. Le spectacle aborde des thèmes du quotidien avec simplicité et créativité. Une sortie idéale pour se détendre et passer un bon moment.', 'https://i.pinimg.com/1200x/d7/38/f7/d738f7ab54a6142e645be74118f7b00e.jpg', 'Salle Culturelle Al Massira', 8, 200.0, 'PUBLIE', 'Rires en Coulisses', 'Agadir'),
(111, 600, 'THEATRE', TIMESTAMP '2025-12-19 21:50:04.292878', TIMESTAMP '2026-01-15 19:00:00', TIMESTAMP '2026-01-15 22:00:00', TIMESTAMP '2025-12-19 21:53:50.750888', 'Une représentation théâtrale captivante interprétée par une troupe professionnelle marocaine. La pièce aborde des thèmes humains profonds comme la famille et le destin, dans une ambiance artistique soignée. Une soirée culturelle idéale pour les amateurs de théâtre classique et contemporain.', 'https://i.pinimg.com/736x/3a/5b/88/3a5b8843d9ccb59d641f87377232332d.jpg', 'Théâtre Mohammed V', 39, 200.0, 'PUBLIE', 'Soirée Théâtre - Le Malentendu', 'Rabat'),
(112, 800, 'CONCERT', TIMESTAMP '2025-12-19 21:53:22.849529', TIMESTAMP '2025-12-12 17:00:00', TIMESTAMP '2025-12-12 21:00:00', TIMESTAMP '2025-12-19 21:53:44.052708', 'Un concert exceptionnel de musique andalouse animé par des artistes renommés. L''événement propose une immersion dans le patrimoine musical marocain avec des performances authentiques. Une soirée riche en émotions et en traditions musicales.', 'https://i.pinimg.com/1200x/78/56/13/785613a2e102187e332b7854d40eea6b.jpg', 'Palais des Arts', 39, 300.0, 'TERMINE', 'Concert Live - Musique Andalouse', 'Casablanca'),
(113, 400, 'CONFERENCE', TIMESTAMP '2025-12-19 21:58:19.684416', TIMESTAMP '2026-01-03 14:00:00', TIMESTAMP '2026-01-03 17:00:00', TIMESTAMP '2025-12-19 21:58:19.684416', 'Une conférence dédiée aux nouvelles technologies, à l''intelligence artificielle et à l''innovation numérique. Des intervenants experts partageront leurs expériences et visions du futur. Un événement idéal pour les étudiants, professionnels et passionnés de technologie.', 'https://i.pinimg.com/736x/d1/b1/f9/d1b1f9dd57e15e27feaf215aa5988a7f.jpg', 'Centre de Conférences Anfa Place', 39, 100.0, 'BROUILLON', 'Conférence - Innovation et Technologies du Futur', 'Casablanca');


-- ============================================
-- RÉSERVATIONS
-- ============================================
-- Colonnes: id, code_reservation, commentaire, date_reservation, montant_total, nombre_places, statut, evenement_id, utilisateur_id

INSERT INTO reservations (id, code_reservation, commentaire, date_reservation, montant_total, nombre_places, statut, evenement_id, utilisateur_id) VALUES
(1, 'EVT-40733', 'good', TIMESTAMP '2025-12-16 23:36:13.337667', 720.0, 4, 'CONFIRMEE', 77, 7),
(5, 'EVT-81697', '', TIMESTAMP '2025-12-17 22:15:16.71053', 200.0, 1, 'ANNULEE', 109, 7),
(9, 'EVT-65061', '', TIMESTAMP '2025-12-18 21:23:26.130316', 200.0, 1, 'EN_ATTENTE', 88, 7),
(10, 'EVT-14364', '', TIMESTAMP '2025-12-18 21:36:22.27519', 360.0, 2, 'EN_ATTENTE', 77, 4),
(11, 'EVT-77332', '', TIMESTAMP '2025-12-18 21:36:54.289602', 300.0, 1, 'ANNULEE', 81, 4),
(12, 'EVT-39548', '', TIMESTAMP '2025-12-18 21:37:19.401569', 800.0, 4, 'CONFIRMEE', 109, 4),
(13, 'EVT-41753', '', TIMESTAMP '2025-12-18 21:38:02.442969', 600.0, 1, 'CONFIRMEE', 87, 4),
(14, 'EVT-15583', '', TIMESTAMP '2025-12-19 22:13:25.131828', 360.0, 2, 'CONFIRMEE', 77, 41),
(15, 'EVT-36932', '', TIMESTAMP '2025-12-19 22:13:40.292899', 200.0, 1, 'EN_ATTENTE', 111, 41),
(16, 'EVT-44958', '', TIMESTAMP '2025-12-19 22:13:54.554459', 900.0, 3, 'ANNULEE', 81, 41),
(17, 'EVT-25158', '', TIMESTAMP '2025-12-19 22:14:05.649612', 750.0, 5, 'CONFIRMEE', 82, 41),
(18, 'EVT-19788', '', TIMESTAMP '2025-12-19 22:14:20.364172', 500.0, 2, 'CONFIRMEE', 80, 41),
(19, 'EVT-41615', '', TIMESTAMP '2025-12-19 22:14:49.383702', 600.0, 1, 'CONFIRMEE', 87, 41),
(20, 'EVT-38131', '', TIMESTAMP '2025-12-19 22:15:22.958074', 1000.0, 4, 'EN_ATTENTE', 80, 7),
(21, 'EVT-87844', '', TIMESTAMP '2025-12-19 22:15:40.466367', 1200.0, 2, 'CONFIRMEE', 87, 7),
(22, 'EVT-70676', '', TIMESTAMP '2025-12-19 22:16:12.856877', 800.0, 10, 'CONFIRMEE', 89, 7),
(23, 'EVT-37054', '', TIMESTAMP '2025-12-19 22:16:23.610195', 150.0, 1, 'EN_ATTENTE', 82, 7),
(24, 'EVT-28407', '', TIMESTAMP '2025-12-19 22:17:06.039551', 500.0, 2, 'CONFIRMEE', 80, 4),
(25, 'EVT-80864', '', TIMESTAMP '2025-12-19 22:17:56.190854', 400.0, 5, 'EN_ATTENTE', 89, 4),
(26, 'EVT-80657', '', TIMESTAMP '2025-12-19 22:18:34.908171', 900.0, 3, 'CONFIRMEE', 85, 40),
(27, 'EVT-96660', '', TIMESTAMP '2025-12-19 22:18:41.957175', 180.0, 1, 'CONFIRMEE', 77, 40),
(28, 'EVT-17434', '', TIMESTAMP '2025-12-19 22:18:53.053761', 2400.0, 4, 'EN_ATTENTE', 87, 40),
(29, 'EVT-44446', '', TIMESTAMP '2025-12-19 22:19:05.898686', 600.0, 2, 'ANNULEE', 81, 40),
(30, 'EVT-46396', '', TIMESTAMP '2025-12-19 22:23:49.538538', 600.0, 2, 'CONFIRMEE', 81, 4);


-- ============================================
-- MESSAGES DE CONTACT
-- ============================================
-- Colonnes: id, email, is_important, is_read, message, name, read_at, sent_at, subject

INSERT INTO contact_messages (id, email, is_important, is_read, message, name, read_at, sent_at, subject) VALUES
(6, 'Alaoui@gmail.com', TRUE, TRUE, 'Je souhaiterais avoir plus d''informations concernant les événements disponibles à la réservation sur votre plateforme. Pouvez-vous me préciser les types d''événements proposés et les modalités de réservation ? Merci d''avance pour votre réponse. Cordialement.', 'Aya Alaoui', TIMESTAMP '2025-12-18 23:42:41.973183', TIMESTAMP '2025-12-18 21:33:28.235184', 'Demande d''information générale'),
(7, 'FatimaMeftah@gmail.com', FALSE, FALSE, 'Bonjour, J''ai rencontré un problème lors de la réservation d''un événement sur votre application. Le paiement semble avoir été effectué, mais la réservation n''apparaît pas dans mon espace personnel. Pouvez-vous vérifier ma situation, s''il vous plaît ? Merci pour votre aide.', 'Fatima Meftah', NULL, TIMESTAMP '2025-12-18 21:34:06.795474', 'Problème de réservation'),
(8, 'nouhaila@gmail.com', FALSE, FALSE, 'Je trouve votre application très intéressante et facile à utiliser. J''aimerais toutefois suggérer l''ajout d''une fonctionnalité permettant de recevoir des notifications lors de la publication de nouveaux événements. Merci de prendre en considération ma suggestion.', 'nouhaila el majdoubi', NULL, TIMESTAMP '2025-12-18 21:35:01.486829', 'Suggestion d''amélioration'),
(9, 'karimbarrada@gmail.com', FALSE, TRUE, 'Bonjour, Depuis quelques jours, j''ai des difficultés à me connecter à mon compte sur votre plateforme. Un message d''erreur s''affiche après la saisie de mes identifiants. Merci de bien vouloir m''aider à résoudre ce problème. Cordialement.', 'Karim barrada', TIMESTAMP '2025-12-18 23:43:11.313916', TIMESTAMP '2025-12-18 21:35:45.175905', 'Demande de support technique');