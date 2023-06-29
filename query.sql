#1)
SELECT drivers.forename, drivers.surname, COUNT(*) AS CantCampeonatos
FROM drivers
JOIN (
    # puntaje total de un piloto por año
    SELECT results.driverId, races.year, SUM(results.points) AS Puntaje
    FROM races
    JOIN results ON races.raceId = results.raceId
    GROUP BY results.driverId, races.year
) AS nombrePuntaje ON drivers.driverId = nombrePuntaje.driverId
JOIN (
    # puntaje del piloto que hizo mas puntos por año
    SELECT nombreMax.year, MAX(nombreMax.Puntaje) AS MaxPuntaje
    FROM (
        # el mismo puntaje de antes
        SELECT results.driverId, races.year, SUM(results.points) AS Puntaje
        FROM races
        JOIN results ON races.raceId = results.raceId
        GROUP BY results.driverId, races.year
    ) AS nombreMax
    GROUP BY nombreMax.year
) AS nombreMaxAnio ON nombreMaxAnio.year = nombrePuntaje.year
WHERE nombreMaxAnio.year < 2023
AND nombrePuntaje.Puntaje = nombreMaxAnio.MaxPuntaje
GROUP BY drivers.forename, drivers.surname
HAVING COUNT(*) = (
    SELECT MAX(T.CantCampeonatos)
    FROM (
        SELECT drivers.forename, drivers.surname, COUNT(*) AS CantCampeonatos
        FROM drivers
    JOIN (
        SELECT results.driverId, races.year, SUM(results.points) AS Puntaje
        FROM races
        JOIN results ON races.raceId = results.raceId
        GROUP BY results.driverId, races.year
    ) AS nombrePuntaje ON drivers.driverId = nombrePuntaje.driverId
JOIN (
    SELECT nombreMax.year, MAX(nombreMax.Puntaje) AS MaxPuntaje
    FROM (
        SELECT results.driverId, races.year, SUM(results.points) AS Puntaje
        FROM races
        JOIN results ON races.raceId = results.raceId
        GROUP BY results.driverId, races.year
    ) AS nombreMax
    GROUP BY nombreMax.year
) AS nombreMaxAnio ON nombreMaxAnio.year = nombrePuntaje.year
WHERE nombreMaxAnio.year < 2023
  AND nombrePuntaje.Puntaje = nombreMaxAnio.MaxPuntaje
GROUP BY drivers.forename, drivers.surname
) AS T)
ORDER BY CantCampeonatos DESC;


#2)
# nombres de los pilotos y la cant de campeonatos
SELECT constructors.name, COUNT(*) AS CantCampeonatos
FROM constructors
JOIN (
    # puntaje total de un piloto por año
    SELECT constructor_results.constructorId, races.year, SUM(constructor_results.points) AS Puntaje
    FROM races
    JOIN constructor_results ON races.raceId = constructor_results.raceId
    GROUP BY constructor_results.constructorId, races.year
) AS nombrePuntaje ON constructors.constructorId = nombrePuntaje.constructorId
JOIN (
    # puntaje del piloto que hizo mas puntos por año
    SELECT nombreMax.year, MAX(nombreMax.Puntaje) AS MaxPuntaje
    FROM (
    	# el mismo puntaje de antes
        SELECT constructor_results.constructorId, races.year, SUM(constructor_results.points) AS Puntaje
        FROM races
        JOIN constructor_results ON races.raceId = constructor_results.raceId
        GROUP BY constructor_results.constructorId, races.year
    ) AS nombreMax
    GROUP BY nombreMax.year
) AS nombreMaxAnio ON nombreMaxAnio.year = nombrePuntaje.year
# 2023 no tiene campeon todavia
WHERE nombreMaxAnio.year < 2023
# se toma el puntaje solamente del que gano la temporada
AND nombrePuntaje.Puntaje = nombreMaxAnio.MaxPuntaje
GROUP BY constructors.name
# cuento cuantos campeonatos ganaron los que ganaron campeonatos
HAVING COUNT(*) = (
	# (de los que ganaron campeonatos devolver el max)
	SELECT MAX(T.CantCampeonatos)
	FROM (
		SELECT constructors.name, COUNT(*) AS CantCampeonatos
		FROM constructors
	JOIN (
    	SELECT constructor_results.constructorId, races.year, SUM(constructor_results.points) AS Puntaje
    	FROM races
    	JOIN constructor_results ON races.raceId = constructor_results.raceId
    	GROUP BY constructor_results.constructorId, races.year
	) AS nombrePuntaje ON constructors.constructorId = nombrePuntaje.constructorId
JOIN (
    SELECT nombreMax.year, MAX(nombreMax.Puntaje) AS MaxPuntaje
    FROM (
        SELECT constructor_results.constructorId, races.year, SUM(constructor_results.points) AS Puntaje
        FROM races
        JOIN constructor_results ON races.raceId = constructor_results.raceId
        GROUP BY constructor_results.constructorId, races.year
    ) AS nombreMax
    GROUP BY nombreMax.year
) AS nombreMaxAnio ON nombreMaxAnio.year = nombrePuntaje.year
WHERE nombreMaxAnio.year < 2023
AND nombrePuntaje.Puntaje = nombreMaxAnio.MaxPuntaje
GROUP BY constructors.name
) AS T)
ORDER BY CantCampeonatos DESC;

#3)
SELECT drivers.forename, drivers.surname, COUNT(results.`position`)
AS carreras_ganadas
FROM drivers
JOIN results ON drivers.driverId = results.driverId
WHERE results.`position` = 1
GROUP BY drivers.forename, drivers.surname
ORDER BY carreras_ganadas DESC

#4)
# es la cantidad total de grand prix que se corrieron?
SELECT races.`year`, races.name
FROM races
WHERE races.`year` BETWEEN 1996 AND 1999

# o cuales fueron los grand prix (distinct) en esos años?
SELECT DISTINCT races.name
FROM races
WHERE races.`year` BETWEEN 1996 AND 1999


#5)
SELECT drivers.forename, drivers.surname
FROM drivers
JOIN results ON drivers.driverId = results.driverId
JOIN races ON results.raceId = races.raceId
WHERE races.`year` = 1997
AND name = 'Japanese Grand Prix'
AND results.`position` = 1;

#6)
SELECT COUNT(*)
FROM races
JOIN results ON results.raceId = races.raceId
JOIN drivers ON results.driverId = drivers.driverId
AND drivers.forename = 'Jacques'
WHERE drivers.surname = 'Villeneuve'
AND results.`position` = 1

#7)
SELECT drivers.forename, drivers.surname, COUNT(*)
FROM drivers
JOIN results ON drivers.driverId = results.driverId
WHERE results.grid <> 1
AND results.`position` = 1
GROUP BY drivers.forename, drivers.surname
LIMIT 1;

#8)
SELECT drivers.forename, drivers.surname, COUNT(*)
FROM drivers
JOIN results ON drivers.driverId = results.driverId
WHERE results.grid = 1
AND results.`position` = 1
GROUP BY drivers.forename, drivers.surname
LIMIT 1;
