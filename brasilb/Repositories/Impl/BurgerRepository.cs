using Npgsql;
using BrasilBurger.Models;
using BrasilBurger.Repositories.Interfaces;
using BrasilBurger.Data;

namespace BrasilBurger.Repositories.Impl
{
    public class BurgerRepository : IBurgerRepository
    {
        private readonly DatabaseConfig _dbConfig;

    public BurgerRepository(DatabaseConfig dbConfig)
    {
        _dbConfig = dbConfig;
    }

        public List<Burger> GetAll()
        {
            var burgers = new List<Burger>();
            
            using (var conn =  _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"SELECT id_burger, nom, prix, image, archive, 
                                date_creation, date_modification 
                                FROM burger ORDER BY nom";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                using (var reader = cmd.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        burgers.Add(MapBurger(reader));
                    }
                }
            }
            
            return burgers;
        }

        public List<Burger> GetNonArchived()
        {
            var burgers = new List<Burger>();
            
            using (var conn =  _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"SELECT id_burger, nom, prix, image, archive, 
                                date_creation, date_modification 
                                FROM burger 
                                WHERE archive = false 
                                ORDER BY nom";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                using (var reader = cmd.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        burgers.Add(MapBurger(reader));
                    }
                }
            }
            
            return burgers;
        }

        public Burger? GetById(int id)
        {
            using (var conn =  _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"SELECT id_burger, nom, prix, image, archive, 
                                date_creation, date_modification 
                                FROM burger 
                                WHERE id_burger = @id";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@id", id);
                    
                    using (var reader = cmd.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            return MapBurger(reader);
                        }
                    }
                }
            }
            
            return null;
        }

       

        private Burger MapBurger(NpgsqlDataReader reader)
        {
            return new Burger
            {
                IdBurger = reader.GetInt32(0),
                Nom = reader.GetString(1),
                Prix = reader.GetDecimal(2),
                Image = reader.IsDBNull(3) ? null : reader.GetString(3),
                Archive = reader.GetBoolean(4),
                DateCreation = reader.GetDateTime(5),
                DateModification = reader.GetDateTime(6)
            };
        }
    }
}