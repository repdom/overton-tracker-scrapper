-- Stores the specific URL path prefixes to query per outlet
-- Each row = one CDX query (path prefix that returns results without timeout)
CREATE TABLE media_outlet_paths (
    id SERIAL PRIMARY KEY,
    media_id INTEGER NOT NULL REFERENCES media_outlets(id),
    path_prefix VARCHAR(500) NOT NULL,  -- e.g. "heritage.org/research/"
    is_active BOOLEAN DEFAULT TRUE
);

-- Verified working paths (tested against CDX API, respond < 3s)
-- Heritage Foundation
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(1, 'heritage.org/research/'),
(1, 'heritage.org/commentary/');

-- Center for American Progress
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(2, 'americanprogress.org/issues/economy/'),
(2, 'americanprogress.org/issues/healthcare/'),
(2, 'americanprogress.org/issues/security/');

-- Cato Institute
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(3, 'cato.org/pubs/pas/'),
(3, 'cato.org/pubs/bp/'),
(3, 'cato.org/commentary/');

-- Brookings Institution
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(4, 'brookings.edu/papers/'),
(4, 'brookings.edu/opinions/');

-- AEI
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(5, 'aei.org/publication/'),
(5, 'aei.org/papers/');

-- EPI
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(6, 'epi.org/publication/'),
(6, 'epi.org/briefingpapers/');

-- Hoover Institution
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(7, 'hoover.org/research/'),
(7, 'hoover.org/publications/');

-- CFR
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(8, 'cfr.org/backgrounder/'),
(8, 'cfr.org/report/');

-- CSIS
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(9, 'csis.org/analysis/'),
(9, 'csis.org/publication/');

-- Hudson Institute
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(10, 'hudson.org/research/'),
(10, 'hudson.org/content/');

-- RAND
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(11, 'rand.org/pubs/research_reports/'),
(11, 'rand.org/pubs/monographs/');

-- Urban Institute
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(12, 'urban.org/research/publication/'),
(12, 'urban.org/publications/');

-- AP
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(13, 'apnews.com/article/');

-- Breitbart
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(14, 'breitbart.com/politics/'),
(14, 'breitbart.com/national-security/'),
(14, 'breitbart.com/economy/');

-- CNN
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(15, 'cnn.com/2007/POLITICS/'),
(15, 'cnn.com/2007/US/'),
(15, 'cnn.com/2007/WORLD/');

-- Daily Kos
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(16, 'dailykos.com/story/');

-- Fox News
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(17, 'foxnews.com/story/'),
(17, 'foxnews.com/politics/'),
(17, 'foxnews.com/us/');

-- The Hill
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(18, 'thehill.com/policy/'),
(18, 'thehill.com/homenews/');

-- HuffPost
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(19, 'huffpost.com/entry/'),
(19, 'huffingtonpost.com/entry/');

-- NYT (CDX returns 403 for nytimes.com — skip, use live scraping only)
-- Washington Times
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(21, 'washingtontimes.com/news/'),
(21, 'washingtontimes.com/commentary/');

-- Washington Post
INSERT INTO media_outlet_paths (media_id, path_prefix) VALUES
(22, 'washingtonpost.com/politics/'),
(22, 'washingtonpost.com/national/');
