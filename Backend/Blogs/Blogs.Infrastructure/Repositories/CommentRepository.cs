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

        public List<Comment> GetByBlogId(Guid blogId)
        {
            return _context.Comments
                .Where(c => c.BlogId == blogId)
                .ToList();
        }
    }
}
