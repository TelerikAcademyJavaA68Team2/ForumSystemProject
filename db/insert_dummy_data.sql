-- Insert dummy data into the 'tags' table (expanded)
INSERT INTO forum_management_system.tags (name)
VALUES
    ('sports cars'), ('classic cars'), ('electric vehicles'), ('car maintenance'), ('luxury cars'),
    ('motorcycles'), ('car modifications'), ('tuning'), ('formula 1'), ('electric car industry'),
    ('car culture'), ('off-road vehicles'), ('car restoration'), ('performance cars'), ('electric car charging'),
    ('muscle cars'), ('supercars'), ('road trips'), ('car insurance'), ('drifting'), ('BMW'), ('E60'), ('UAZ'), ('MOSKVICH');

-- for testing with the dummy data the PASSWORD is encoded so for logIn use any username and 12345678 as password or feel free to register!
INSERT INTO forum_management_system.users (first_name, last_name, email, username, password, is_admin, is_blocked, profile_photo, phone_number)
VALUES
    ('Georgi', 'Benchev', 'GeogriBenchev@gmail.com', 'georgi', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 1, 0, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQi2Mm5P8j09P4hPKa1B-t9eIOHzHmR7IBkw&s', '123-456-7890'),
    ('Ivan', 'Ivanov', 'Vankata@gmail.com', 'vankata', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 1, 0, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0G6ROpWaqElsf9V0LUgMhhC52ljEwK77eMA&s', '098-765-4321'),
    ('Stoyanka', 'Karashtranova', 'tancheto@gmail.com', 'tancheto', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 1, 0, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDpWYsLSeY1sLvwgFNwBeJGjszUfEofDpwJw&s', '555-555-5555'),
    ('ElitsaUser', 'Savova', 'elitsaUser@gmail.com', 'elitsa_user', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 0, 0, '/images/default-profile-pic.png', NULL),
    ('Elitsa', 'Savova', 'elitsa@gmail.com', 'elitsa', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 1, 0, '/images/default-profile-pic.png', NULL),
    ('StoyankaUser', 'Karashtranova', 'tanchetoUser@gmail.com', 'tancheto_user', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 0, 0, '/images/default-profile-pic.png', NULL),
    ('Nikolai', 'Todorov', 'nikolai.todorov@gmail.com', 'nikolai', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 1, 0, '/images/default-profile-pic.png', '777-123-4567'),
    ('Petya', 'Petrova', 'petya.petrova@gmail.com', 'petya', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 0, 0, '/images/default-profile-pic.png', '555-666-7777'),
    ('Dimitrov', 'Boris', 'dimitrov.boris@gmail.com', 'dimodinamoto', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 1, 0, '/images/default-profile-pic.png', '123-123-1234'),
    ('Petar', 'Petrov', 'petar@example.com', 'petar', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 0, 0, '/images/default-profile-pic.png', NULL),
    ('Maria', 'Georgieva', 'maria@example.com', 'maria', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 0, 0, '/images/default-profile-pic.png', NULL),
    ('Elena', 'Dimitrova', 'elena@example.com', 'elena', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 0, 0, '/images/default-profile-pic.png', NULL),
    ('Stefan', 'Kostov', 'stefan@example.com', 'stefan', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 0, 1, '/images/default-profile-pic.png', NULL),
    ('Tanya', 'Ivanova', 'tanya@example.com', 'tanya', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 0, 0, '/images/default-profile-pic.png', NULL),
    ('Boris', 'Dimitrov', 'boris@example.com', 'boris', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 1, 0, '/images/default-profile-pic.png', NULL),
    ('Kristina', 'Vasileva', 'kristina@example.com', 'kristina', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 0, 0, '/images/default-profile-pic.png', NULL);

-- Insert dummy data into the 'posts' table (car-related posts, 5x larger dataset)
INSERT INTO forum_management_system.posts (user_id, title, content)
VALUES
    (2, 'Top 10 Sports Cars of 2025', CONCAT('Sports cars are constantly evolving. Here are the top 10 sports cars of 2025 that every car enthusiast should check out. ', 'In this post, we cover the most iconic sports cars for 2025 including the Porsche 911 Turbo, Ferrari 488 GTB, and Lamborghini Huracán.')),
    (2, 'Electric Cars vs Gasoline Cars: Which is Better?', CONCAT('Electric vehicles are becoming more popular, but how do they compare to traditional gasoline-powered cars? ', 'We dive deep into the pros and cons of electric cars, covering aspects such as range, charging infrastructure, and environmental impact.')),
    (3, 'How to Maintain Your Classic Car', CONCAT('Classic car maintenance can be tricky, but with the right tools and knowledge, you can keep your vintage car in top condition. ', 'In this article, we explore the best tips and techniques for keeping your classic car running smoothly and ensuring it holds its value.')),
    (3, 'Tuning Your Car: Everything You Need to Know', CONCAT('Tuning a car can make a huge difference in performance. Let’s break down the basics of car tuning and how it can improve your ride. ', 'We’ll discuss everything from engine tuning to suspension modifications, helping you achieve the perfect balance of power and handling.')),
    (2, 'Formula 1: The Future of Racing', CONCAT('Formula 1 continues to innovate, with new technologies and rules making the sport more exciting than ever. What does the future hold for F1? ', 'From hybrid engines to autonomous tech, we explore what the future of Formula 1 racing could look like and how it will change the sport.')),
    (4, 'Electric Car Industry: Trends in 2025', CONCAT('As electric cars become mainstream, the electric car industry is expected to grow rapidly. This post covers the key trends we expect to see in 2025. ', 'From battery improvements to autonomous driving features, find out how the electric car market will evolve.')),
    (5, 'The Best Tuning Kits for Your Car', CONCAT('Want to take your car’s performance to the next level? Here are some of the best tuning kits available in 2025. ', 'We review the top tuning kits for various types of cars, including performance modifications for both speed and handling.')),
    (6, 'Classic Cars: The Top 5 to Invest In', CONCAT('Looking to invest in classic cars? Here are the top 5 classic cars to add to your collection in 2025. ', 'These vehicles not only offer a fun driving experience but are also likely to increase in value over time.')),
    (2, 'Car Maintenance Hacks Every Owner Should Know', CONCAT('Maintaining your car is essential for its longevity. In this post, we share 10 maintenance hacks every car owner should know. ', 'From oil changes to tire rotations, this guide will help you keep your car running smoothly for years to come.')),
    (3, 'The Rise of Electric Sports Cars', CONCAT('Electric sports cars are the future of high-performance driving. Learn how the industry is rapidly shifting towards eco-friendly, high-powered vehicles. ', 'In this post, we highlight some of the most anticipated electric sports cars set to hit the roads in 2025.')),
    (2, 'How to Maintain a Classic Car', 'Tips on keeping your vintage car in great shape.'),
    (3, 'Electric Vehicles: Are They the Future?', 'Discussion on the growth of electric vehicles.'),
    (4, 'Tuning Basics: How to Boost Your Car’s Performance', 'A guide to getting the best out of your car.'),
    (5, 'Formula 1 - New Regulations in 2025', 'Breaking down the changes coming to F1.'),
    (6, 'Luxury Cars: Are They Worth the Price?', 'Examining the real value behind luxury cars.'),
    (7, 'Off-Roading Adventures: Best Vehicles for the Job', 'Best vehicles and tips for off-roading.'),
    (8, 'Muscle Cars - A Timeless Passion', 'Exploring the world of muscle cars.'),
    (9, 'Car Insurance: Tips to Save Money', 'Ways to reduce car insurance costs.'),
    (10, 'Supercars: The Ultimate Driving Machines', 'Why supercars captivate car lovers.');

-- Insert dummy data into the 'comments' table (comments on posts, limited to posts 1-5)
INSERT INTO comments (id, post_id, user_id, content)
VALUES
    (1, 1, 3, 'I love the Porsche 911 Turbo! Definitely deserves the top spot on this list.'),
    (2, 2, 4, 'Great comparison! I’m leaning towards electric cars for my next purchase.'),
    (3, 3, 5, 'Classic car maintenance is a real challenge, but worth it. I’ve restored a few old Mustangs myself.'),
    (4, 4, 1, 'Tuning makes all the difference. I’ve done a few mods on my Subaru WRX and it’s a completely different beast now!'),
    (5, 5, 5, 'F1 is going to be even more exciting in the future. I can’t wait to see the technological advancements!'),
    (6, 1, 4, 'Classic cars are a work of art.'),
    (7, 2, 5, 'Electric cars are the future, no doubt.'),
    (8, 3, 6, 'Tuning really makes a difference in performance.'),
    (9, 4, 8, 'Luxury cars are just overpriced.'),
    (10, 5, 9, 'Off-roading is my favorite hobby!');

-- Insert dummy data into the 'post_likes_dislikes' table (likes and dislikes limited to posts 1-5)
INSERT INTO forum_management_system.post_likes_dislikes (post_id, user_id, is_like)
VALUES
    (1, 2, 1), (2, 1, 1),
    (3, 4, 1), (4, 3, 1), (5, 5, 1),
    (1, 3, 1), (2, 3, 1),
    (3, 5, 0), (4, 6, 1),
    (5, 7, 1), (1, 5, 0),
    (2, 4, 0), (3, 6, 1),
    (4, 7, 1), (5, 8, 0);


-- Insert dummy data into the 'post_tags' table (tags limited to posts 1-5, all tags used)
INSERT INTO forum_management_system.post_tags (post_id, tag_id)
VALUES
    (1, 1), (1, 2), (1, 3),
    (1, 4), (1, 5), (1, 6),
    (2, 7), (2, 8), (2, 9),
    (2, 10), (2, 3),
    (3, 4), (3, 5), (3, 6), (3, 7), (3, 8),
    (4, 1), (4, 2), (4, 9), (4, 10), (4, 8),
    (5, 6), (5, 7), (5, 3), (5, 4), (5, 5);
