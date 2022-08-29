import { Route, Routes } from "react-router-dom";
import { Footer, NavBar } from "./components/Nav";
import * as React from "react";
import FeedPage from "./FeedPage/FeedPage";
import LoginPage from "./LoginPage/LoginPage";
import { AuthContextProvider } from "./shared/AuthContext";

export default function App() {
  return (
    <AuthContextProvider>
      <div className={"text-amber-900"}>
        <NavBar />
        <Routes>
          <Route path="/" element={<FeedPage />} />
          <Route path="/login" element={<LoginPage />} />
        </Routes>
        <Footer />
      </div>
    </AuthContextProvider>
  );
}
