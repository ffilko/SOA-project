using Blogs.Core.Interfaces;
using Blogs.Core.Model;
using Blogs.Infrastructure.Database;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Blogs.Infrastructure.Repositories
{
    public class LikeRepository:ILikeRepository
    {
        private readonly BlogDbContext _context;

        public LikeRepository(BlogDbContext context)
        {
            _context = context;
        }

        public Like Create(Like like)
        {
            _context.Likes.Add(like);
            _context.SaveChanges();
            return like;
        }


        public Like GetById(Guid id)
        {
            return _context.Likes.FirstOrDefault(b => b.Id == id);
        }

        public List<Like> GetAll()
        {
            return _context.Likes.ToList();
        }

        public void Delete(Like like)
        {
            _context.Likes.Remove(like);
            _context.SaveChanges();
        }
    }
}
