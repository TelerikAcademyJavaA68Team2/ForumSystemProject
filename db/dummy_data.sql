-- Insert dummy data into the 'tags' table (expanded)
INSERT INTO forum_management_system.tags (name)
VALUES
    ('sports cars'),
    ('classic cars'),
    ('electric vehicles'),
    ('car maintenance'),
    ('luxury cars'),
    ('motorcycles'),
    ('car modifications'),
    ('tuning'),
    ('formula 1'),
    ('electric car industry'),
    ('car culture'),
    ('off-road vehicles'),
    ('car restoration'),
    ('performance cars'),
    ('electric car charging');

-- Insert dummy data into the 'users' table (car enthusiasts and admins)
INSERT INTO users (first_name, last_name, email, username, password, is_admin, is_blocked, profile_photo, phone_number)
VALUES
    ('Georgi', 'Benchev', 'GeogriBenchev@gmail.com', 'georgi', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 1, 0, NULL, '123-456-7890'),
    ('Ivan', 'Ivanov', 'Vankata@gmail.com', 'vankata', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 1, 0, NULL, '098-765-4321'),
    ('Stoyanka', 'Karashtranova', 'tancheto@gmail.com', 'tancheto', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 1, 0, NULL, '555-555-5555'),
    ('StoyankaUser', 'Karashtranova', 'tanchetoUser@gmail.com', 'tancheto_user', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 0, 0, NULL, NULL),
    ('GeorgiUser', 'Benchev', 'georgiUser@gmail.com', 'georgi_user', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 0, 0, NULL, NULL),
    ('IvanUser', 'Ivanov', 'vankataUser@gmail.com', 'vankata_user', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 0, 1, NULL, NULL),
    ('Nikolai', 'Todorov', 'nikolai.todorov@gmail.com', 'nikolai', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 1, 0, NULL, '777-123-4567'),
    ('Petya', 'Petrova', 'petya.petrova@gmail.com', 'petya', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 0, 0, NULL, '555-666-7777'),
    ('Dimitrov', 'Boris', 'dimitrov.boris@gmail.com', 'dimitrov', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 1, 0, NULL, '123-123-1234');

-- Insert dummy data into the 'posts' table (car-related posts, 5x larger dataset)
INSERT INTO posts (user_id, title, content)
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
    (3, 'The Rise of Electric Sports Cars', CONCAT('Electric sports cars are the future of high-performance driving. Learn how the industry is rapidly shifting towards eco-friendly, high-powered vehicles. ', 'In this post, we highlight some of the most anticipated electric sports cars set to hit the roads in 2025.'));

-- Insert dummy data into the 'comments' table (comments on posts, expanded)
INSERT INTO comments (post_id, user_id, content)
VALUES
    (1, 3, 'I love the Porsche 911 Turbo! Definitely deserves the top spot on this list.'),
    (2, 4, 'Great comparison! I’m leaning towards electric cars for my next purchase.'),
    (3, 5, 'Classic car maintenance is a real challenge, but worth it. I’ve restored a few old Mustangs myself.'),
    (4, 1, 'Tuning makes all the difference. I’ve done a few mods on my Subaru WRX and it’s a completely different beast now!'),
    (5, 2, 'F1 is going to be even more exciting in the future. I can’t wait to see the technological advancements!'),
    (6, 3, 'Electric cars will be the future of the sports car industry, mark my words!'),
    (7, 4, 'Tuning kits make a huge difference in performance. Definitely recommend trying them out!'),
    (8, 2, 'I’ve been thinking about investing in classic cars, I’ll check out this list.'),
    (9, 5, 'Good tips on car maintenance. I didn’t know some of these tricks!'),
    (10, 6, 'Electric sports cars are taking the world by storm. Can’t wait for the future!');

-- Insert dummy data into the 'post_likes_dislikes' table (likes and dislikes for posts, expanded)
INSERT INTO post_likes_dislikes (post_id, user_id, is_like)
VALUES
    (1, 2, 1),
    (2, 1, 1),
    (3, 4, 1),
    (4, 3, 1),
    (5, 5, 1),
    (1, 4, 0),
    (2, 5, 0),
    (6, 2, 1),
    (7, 3, 1),
    (8, 4, 0);

-- Insert dummy data into the 'post_tags' table (tagging posts with appropriate topics, expanded)
INSERT INTO post_tags (post_id, tag_id)
VALUES
    (1, 1), (7, 6),
    (1, 6), (8, 5),
    (2, 3), (8, 4),
    (2, 4), (8, 8),
    (3, 2),
    (3, 4), (8, 1),
    (4, 7), (8, 8),
    (4, 8), (8, 3),
    (5, 9), (9, 1),
    (6, 9), (10, 6);
