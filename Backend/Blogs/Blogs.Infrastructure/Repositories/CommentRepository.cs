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
    public class CommentRepository : ICommentRepository
    {
        private readonly BlogDbContext _context;

        public CommentRepository(BlogDbContext context)
        {
            _context = context;
        }

        public Comment Create(Comment comment)
        {
            _context.Comments.Add(comment);
            _context.SaveChanges();
            return comment;
        }
        public Comment Update(Comment comment)
        {
            _context.Comments.Update(comment);
            _context.SaveChanges();
            return comment;
        }
        public Comment GetById(Guid id)
        {
            return _context.Comments.FirstOrDefault(b => b.Id == id);
        }

        public List<Comment> GetAll()
        {
            return _context.Comments.ToList();
        }
    }
}
