using Blogs.Core.Interfaces;
using Blogs.Core.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Blogs.Core.Services
{
    public class LikeService : ILikeService
    {
        private readonly ILikeRepository _likeRepository;

        public LikeService(ILikeRepository likeRepository)
        {
            _likeRepository = likeRepository;
        }
        public Like Create(Like like)
        {
            return _likeRepository.Create(like);
        }

        public bool Delete(Guid id)
        {
            var like = _likeRepository.GetById(id);

            if (like == null)
                return false;

            _likeRepository.Delete(like);

            return true;
        }

        public List<Like> GetAll()
        {
            return _likeRepository.GetAll();
        }

        public int GetSumByBlogId(Guid blogId)
        {
            var likes = _likeRepository.GetAll();

            return likes.Count(l => l.BlogId == blogId);
        }
    }
}
