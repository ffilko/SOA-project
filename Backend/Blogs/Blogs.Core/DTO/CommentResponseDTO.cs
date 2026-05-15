using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Blogs.Core.DTO
{
    public class CommentResponseDTO
    {
        public Guid Id { get; set; }
        public Guid UserId { get; set; }
        public Guid BlogId { get; set; }
        public string Text { get; set; }
        public DateTime CreatedAt { get; set; }
        public DateTime LastChange { get; set; }
    }
}
