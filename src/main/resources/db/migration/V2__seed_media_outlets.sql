-- ============================================================
-- SEED: Think Tanks (Emisores)
-- ============================================================
INSERT INTO media_outlets (name, domain, source_type, base_ideology_score) VALUES
('Heritage Foundation',              'heritage.org',          'think_tank',  0.80),
('Center for American Progress',     'americanprogress.org',  'think_tank', -0.70),
('Cato Institute',                   'cato.org',              'think_tank',  0.60),
('Brookings Institution',            'brookings.edu',         'think_tank', -0.20),
('American Enterprise Institute',    'aei.org',               'think_tank',  0.75),
('Economic Policy Institute',        'epi.org',               'think_tank', -0.75),
('Hoover Institution',               'hoover.org',            'think_tank',  0.70),
('Council on Foreign Relations',     'cfr.org',               'think_tank',  0.00),
('CSIS',                             'csis.org',              'think_tank',  0.05),
('Hudson Institute',                 'hudson.org',            'think_tank',  0.72),
('RAND Corporation',                 'rand.org',              'think_tank',  0.00),
('Urban Institute',                  'urban.org',             'think_tank', -0.30);

-- ============================================================
-- SEED: Media Outlets (Espejos)
-- ============================================================
INSERT INTO media_outlets (name, domain, source_type, base_ideology_score) VALUES
('Associated Press',     'apnews.com',          'news',  0.00),
('Breitbart',            'breitbart.com',        'news',  0.90),
('CNN',                  'cnn.com',              'news', -0.55),
('Daily Kos',            'dailykos.com',         'blog', -0.85),
('Fox News',             'foxnews.com',          'news',  0.80),
('The Hill',             'thehill.com',          'news',  0.00),
('HuffPost',             'huffpost.com',         'news', -0.65),
('New York Times',       'nytimes.com',          'news', -0.50),
('USA Today',            'usatoday.com',         'news', -0.05),
('Washington Times',     'washingtontimes.com',  'news',  0.70),
('Washington Post',      'washingtonpost.com',   'news', -0.55);
