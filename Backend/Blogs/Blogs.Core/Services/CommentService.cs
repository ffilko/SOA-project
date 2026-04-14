using Blogs.Core.Interfaces;
using Blogs.Core.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Comments.Core.Services
{
    public class CommentService:ICommentService
    {
        private readonly ICommentRepository _commentRepository;

        public CommentService(ICommentRepository commentRepository)
        {
            _commentRepository = commentRepository;
        }

        public Comment Create(Comment comment)
        {
            return _commentRepository.Create(comment);
        }

        public Comment Update(Comment comment)
        {
            comment.LastChange = DateTime.UtcNow;

            _commentRepository.Update(comment);

            return comment;
        }

        public Comment GetById(Guid id)
        {
            return _commentRepository.GetById(id);
        }

        public List<Comment> GetAll()
        {
            return _commentRepository.GetAll();
        }
    }
}
