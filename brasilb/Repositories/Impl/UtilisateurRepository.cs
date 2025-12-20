using Npgsql;
using BrasilBurger.Models;
using BrasilBurger.Repositories.Interfaces;
using BrasilBurger.Data;

namespace BrasilBurger.Repositories.Impl
{
    public class UtilisateurRepository : IUtilisateurRepository
    {
        private readonly DatabaseConfig _dbConfig;

        public UtilisateurRepository(DatabaseConfig dbConfig)
        {
            _dbConfig = dbConfig;
        }

        public Utilisateur? GetById(int id)
        {
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"SELECT id_utilisateur, nom, prenom, telephone, email, 
                                mot_de_passe, role, adresse, date_creation 
                                FROM utilisateur 
                                WHERE id_utilisateur = @id";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@id", id);
                    
                    using (var reader = cmd.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            return MapUtilisateur(reader);
                        }
                    }
                }
            }
            
            return null;
        }

        public Utilisateur? GetByEmail(string email)
        {
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"SELECT id_utilisateur, nom, prenom, telephone, email, 
                                mot_de_passe, role, adresse, date_creation 
                                FROM utilisateur 
                                WHERE email = @email";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@email", email);
                    
                    using (var reader = cmd.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            return MapUtilisateur(reader);
                        }
                    }
                }
            }
            
            return null;
        }

        public Utilisateur? GetByTelephone(string telephone)
        {
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"SELECT id_utilisateur, nom, prenom, telephone, email, 
                                mot_de_passe, role, adresse, date_creation 
                                FROM utilisateur 
                                WHERE telephone = @telephone";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@telephone", telephone);
                    
                    using (var reader = cmd.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            return MapUtilisateur(reader);
                        }
                    }
                }
            }
            
            return null;
        }

        public int Create(Utilisateur utilisateur)
        {
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"
                    INSERT INTO utilisateur (nom, prenom, telephone, email, 
                                            mot_de_passe, role, adresse)
                    VALUES (@nom, @prenom, @telephone, @email, @motDePasse, @role, @adresse)
                    RETURNING id_utilisateur";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@nom", utilisateur.Nom);
                    cmd.Parameters.AddWithValue("@prenom", utilisateur.Prenom);
                    cmd.Parameters.AddWithValue("@telephone", utilisateur.Telephone);
                    cmd.Parameters.AddWithValue("@email", utilisateur.Email);
                    cmd.Parameters.AddWithValue("@motDePasse", utilisateur.MotDePasse);
                    cmd.Parameters.AddWithValue("@role", utilisateur.Role); // ✅ Maintenant ça marche grâce au mapping
                    cmd.Parameters.AddWithValue("@adresse", (object?)utilisateur.Adresse ?? DBNull.Value);
                    
                    return (int)cmd.ExecuteScalar()!;
                }
            }
        }

        public void Update(Utilisateur utilisateur)
        {
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"
                    UPDATE utilisateur 
                    SET nom = @nom, prenom = @prenom, telephone = @telephone, 
                        email = @email, adresse = @adresse
                    WHERE id_utilisateur = @id";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@id", utilisateur.IdUtilisateur);
                    cmd.Parameters.AddWithValue("@nom", utilisateur.Nom);
                    cmd.Parameters.AddWithValue("@prenom", utilisateur.Prenom);
                    cmd.Parameters.AddWithValue("@telephone", utilisateur.Telephone);
                    cmd.Parameters.AddWithValue("@email", utilisateur.Email);
                    cmd.Parameters.AddWithValue("@adresse", (object?)utilisateur.Adresse ?? DBNull.Value);
                    
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public bool EmailExists(string email)
        {
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = "SELECT COUNT(*) FROM utilisateur WHERE email = @email";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@email", email);
                    return (long)cmd.ExecuteScalar()! > 0;
                }
            }
        }

        public bool TelephoneExists(string telephone)
        {
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = "SELECT COUNT(*) FROM utilisateur WHERE telephone = @telephone";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@telephone", telephone);
                    return (long)cmd.ExecuteScalar()! > 0;
                }
            }
        }

        private Utilisateur MapUtilisateur(NpgsqlDataReader reader)
        {
            return new Utilisateur
            {
                IdUtilisateur = reader.GetInt32(0),
                Nom = reader.GetString(1),
                Prenom = reader.GetString(2),
                Telephone = reader.GetString(3),
                Email = reader.GetString(4),
                MotDePasse = reader.GetString(5),
                Role = reader.GetFieldValue<RoleUtilisateur>(6), // ✅ Grâce au mapping global
                Adresse = reader.IsDBNull(7) ? null : reader.GetString(7),
                DateCreation = reader.GetDateTime(8)
            };
        }
    }
}