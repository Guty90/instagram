import { useState, useEffect } from "react";
import { toggleLike, getLikeCount, hasLiked } from "../api/likeApi";
import { useAuth } from "../context/authContext";
import LikesModal from "./LikesModal";
import "../styles/Stories.css";

const StoryViewer = ({ stories, initialIndex, onClose }) => {
  const { user } = useAuth();
  const [currentIndex, setCurrentIndex] = useState(initialIndex);
  const [liked, setLiked] = useState(false);
  const [likeCount, setLikeCount] = useState(0);
  const [loading, setLoading] = useState(false);
  const [progressKey, setProgressKey] = useState(0);
  const [showLikes, setShowLikes] = useState(false);

  const story = stories[currentIndex];
  const totalStories = stories.length;
  const isOwnStory = Number(story.userId) === Number(user.userId);

  useEffect(() => {
    if (!story) return;
    console.log("story.userId:", story.userId, typeof story.userId);
    console.log("user.userId:", user.userId, typeof user.userId);
    console.log("isOwnStory:", isOwnStory);

    getLikeCount(story.id)
      .then(setLikeCount)
      .catch(() => {});
    if (!isOwnStory) {
      hasLiked(story.id, user.userId)
        .then(setLiked)
        .catch(() => {});
    }
    setProgressKey((prev) => prev + 1);
    setShowLikes(false);
  }, [currentIndex, story?.id]);

  useEffect(() => {
    if (showLikes) return;
    const timer = setTimeout(() => {
      if (currentIndex < stories.length - 1) {
        setCurrentIndex((prev) => prev + 1);
      } else {
        onClose();
      }
    }, 5000);
    return () => clearTimeout(timer);
  }, [currentIndex, showLikes]);

  useEffect(() => {
    const handleKey = (e) => {
      if (e.key === "Escape") {
        if (showLikes) setShowLikes(false);
        else onClose();
      }
      if (!showLikes) {
        if (e.key === "ArrowRight" && currentIndex < stories.length - 1)
          setCurrentIndex((prev) => prev + 1);
        if (e.key === "ArrowLeft" && currentIndex > 0)
          setCurrentIndex((prev) => prev - 1);
      }
    };
    window.addEventListener("keydown", handleKey);
    return () => window.removeEventListener("keydown", handleKey);
  }, [currentIndex, showLikes]);

  const handleLike = async () => {
    if (loading) return;
    setLoading(true);
    try {
      const res = await toggleLike(story.id, user.userId);
      if (res === null) {
        setLiked(false);
        setLikeCount((prev) => prev - 1);
      } else {
        setLiked(true);
        setLikeCount((prev) => prev + 1);
      }
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const formatTime = (dateStr) => {
    const diff = Date.now() - new Date(dateStr).getTime();
    const mins = Math.floor(diff / 60000);
    if (mins < 60) return `${mins}m`;
    const hrs = Math.floor(mins / 60);
    if (hrs < 24) return `${hrs}h`;
    return `${Math.floor(hrs / 24)}d`;
  };

  const prevStory = stories[currentIndex - 1];
  const nextStory = stories[currentIndex + 1];

  return (
    <>
      <div className="story-viewer-overlay" onClick={onClose}>
        {/* Historia izquierda */}
        {prevStory && (
          <div
            className="story-viewer-side story-viewer-side--left"
            onClick={(e) => {
              e.stopPropagation();
              setCurrentIndex(currentIndex - 1);
            }}
          >
            {prevStory.imageUrl ? (
              <img
                src={prevStory.imageUrl}
                className="story-viewer-side__image"
                alt=""
              />
            ) : (
              <div className="story-viewer-side__placeholder" />
            )}
          </div>
        )}

        {/* Historia principal */}
        <div className="story-viewer" onClick={(e) => e.stopPropagation()}>
          <div
            className="story-viewer__progress"
            style={{ display: "flex", gap: 4 }}
          >
            {stories.map((_, i) => (
              <div
                key={i}
                className="story-viewer__progress-bar"
                style={{ flex: 1 }}
              >
                {i < currentIndex && (
                  <div
                    className="story-viewer__progress-fill"
                    style={{ width: "100%", animation: "none" }}
                  />
                )}
                {i === currentIndex && (
                  <div
                    key={progressKey}
                    className="story-viewer__progress-fill"
                  />
                )}
              </div>
            ))}
          </div>

          <div className="story-viewer__header">
            <div className="story-viewer__user-avatar">
              {story.imageUrl ? (
                <img
                  src={story.imageUrl}
                  alt=""
                  style={{
                    width: "100%",
                    height: "100%",
                    objectFit: "cover",
                    borderRadius: "50%",
                  }}
                />
              ) : (
                (story.username || `u${story.userId}`).charAt(0).toUpperCase()
              )}
            </div>
            <div className="story-viewer__user-info">
              <div className="story-viewer__username">
                {story.username || `user_${story.userId}`}
              </div>
              <div className="story-viewer__time">
                {formatTime(story.createdAt)}
              </div>
            </div>
            <button className="story-viewer__close" onClick={onClose}>
              ✕
            </button>
          </div>

          {story.imageUrl ? (
            <img
              src={story.imageUrl}
              className="story-viewer__image"
              alt="story"
            />
          ) : (
            <div className="story-viewer__image-placeholder" />
          )}

          <div className="story-viewer__content">
            {story.content && (
              <p className="story-viewer__text">{story.content}</p>
            )}
            <div className="story-viewer__actions">
              {isOwnStory ? (
                <button
                  className="story-viewer__eye-btn"
                  onClick={() => setShowLikes(true)}
                >
                  👁️
                  <span className="story-viewer__eye-count">
                    {likeCount} {likeCount === 1 ? "like" : "likes"}
                  </span>
                </button>
              ) : (
                <button
                  className={`story-viewer__like-btn ${liked ? "story-viewer__like-btn--liked" : ""}`}
                  onClick={handleLike}
                >
                  {liked ? "❤️" : "🤍"}
                  <span className="story-viewer__like-count">{likeCount}</span>
                </button>
              )}
            </div>
          </div>

          {currentIndex > 0 && (
            <button
              className="story-viewer__nav story-viewer__nav--prev"
              onClick={() => setCurrentIndex((prev) => prev - 1)}
            >
              ‹
            </button>
          )}
          {currentIndex < stories.length - 1 && (
            <button
              className="story-viewer__nav story-viewer__nav--next"
              onClick={() => setCurrentIndex((prev) => prev + 1)}
            >
              ›
            </button>
          )}
        </div>

        {/* Historia derecha */}
        {nextStory && (
          <div
            className="story-viewer-side story-viewer-side--right"
            onClick={(e) => {
              e.stopPropagation();
              setCurrentIndex(currentIndex + 1);
            }}
          >
            {nextStory.imageUrl ? (
              <img
                src={nextStory.imageUrl}
                className="story-viewer-side__image"
                alt=""
              />
            ) : (
              <div className="story-viewer-side__placeholder" />
            )}
          </div>
        )}
      </div>

      {showLikes && (
        <LikesModal
          storyId={story.id}
          likeCount={likeCount}
          onClose={() => setShowLikes(false)}
        />
      )}
    </>
  );
};

export default StoryViewer;
