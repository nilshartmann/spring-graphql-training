import { Route, Routes } from "react-router-dom";
import { Footer, NavBar } from "./components/Nav";
import * as React from "react";
import FeedPage from "./FeedPage/FeedPage";
import LoginPage from "./LoginPage/LoginPage";
import { AuthContextProvider } from "./shared/AuthContext";
import StoryPage from "./StoryPage/StoryPage";
import PageLayout from "./components/PageLayout";
import NotFoundPage from "./NotFoundPage";

export default function App() {
  return (
    <AuthContextProvider>
      <div className={"text-amber-900"}>
        <NavBar />
        <PageLayout>
          <Routes>
            <Route path="/" element={<FeedPage />} />
            <Route path="/s/:storyId" element={<StoryPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="*" element={<NotFoundPage />} />
          </Routes>
        </PageLayout>
        <Footer />
      </div>
    </AuthContextProvider>
  );
}
