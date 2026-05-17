using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Blogs.Core.DTO
{
    public class CreateCommentDTO
    {
        public Guid UserId { get; set; }
        public string Content { get; set; }
    }
}
