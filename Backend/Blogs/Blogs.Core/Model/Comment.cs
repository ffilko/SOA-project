using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static System.Net.Mime.MediaTypeNames;

namespace Blogs.Core.Model
{
    public class Comment
    {
        public Guid Id { get; set; }
        public Guid UserId { get; set; }
        public Guid BlogId { get; set; }
        public string Text { get; set; }
        public DateTime CreatedAt { get; set; }
        public DateTime LastChange { get; set; }

        public Comment()
        {
            Id = Guid.NewGuid();
            CreatedAt = DateTime.UtcNow;
            LastChange = DateTime.UtcNow;
        }

    }
}
