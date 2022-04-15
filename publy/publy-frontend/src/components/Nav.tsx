/* eslint-disable jsx-a11y/anchor-is-valid */
import * as React from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import Button from "./Button";
import { LogoutIcon } from "@heroicons/react/outline";
import { useMe } from "../shared/useMe";
import { useAuthContext } from "../shared/AuthContext";
import { graphiqlUrl } from "../urls";

function UserProfile() {
  const { token } = useAuthContext();
  const navigate = useNavigate();
  const { pathname } = useLocation();

  console.log("path", pathname);

  if (!token) {
    return (
      <Button
        onClick={() => navigate("/login", { state: { redirectTo: pathname } })}
        small
      >
        Login
      </Button>
    );
  }

  return <UserBadge />;
}

function UserBadge() {
  const { me, error } = useMe();
  const { setToken } = useAuthContext();

  if (error) {
    return (
      <div className={"flex place-items-center"}>
        <LogoutIcon
          className={"h-7 pl-2 hover:cursor-pointer hover:text-orange-500"}
          onClick={() => setToken("")}
        />
      </div>
    );
  }

  if (!me) {
    return null;
  }

  return (
    <div className={"flex place-items-center"}>
      <Link
        to={`/u/${me.id}`}
        className={"flex hover:cursor-pointer hover:underline"}
      >
        <img
          className={"h-7 pr-2"}
          src={me.profileImageUrl}
          alt={me.user?.name}
        />
        {me.user?.name}
      </Link>
      <LogoutIcon
        className={"h-7 pl-2 hover:cursor-pointer hover:text-orange-500"}
        onClick={() => setToken("")}
      />
    </div>
  );
}

export function NavBar() {
  /*
  https://stackoverflow.com/a/38948646

  ul {
  display: grid;
  grid-template-columns: 1fr repeat(3, auto) 1fr;
  grid-column-gap: 5px;
  justify-items: center;
}

li:nth-child(1) { grid-column-start: 2; }
li:nth-child(4) { margin-left: auto; }

  ul { padding: 0; margin: 0; list-style: none; }
  li { padding: 5px; background: #aaa; }
  p  { text-align: center; }
   */

  return (
    <nav className="bg-orange-50">
      <div className="max-w-4xl mx-auto px-4 py-1 grid grid-cols-[1fr_auto_1fr]">
        <div className="grid place-items-center h-16 col-start-2">
          <Link to={"/"}>
            <div
              className={
                "text-4xl hover:underline hover:decoration-4 hover:decoration-orange-500"
              }
            >
              <span
                className="tracking-widest font-bold"
                style={{ fontFamily: "Lobster, cursive" }}
              >
                Publy{" "}
              </span>
              <span className={"text-base font-metro"}>
                your social publishing network
              </span>
            </div>
          </Link>
        </div>
        <div className={"flex justify-end mt-auto mb-auto"}>
          <UserProfile />
        </div>
      </div>
    </nav>
  );
}

export function Footer() {
  return (
    <footer className="bg-orange-50">
      <div className="max-w-4xl mx-auto px-4 py-1 grid grid-cols-[1fr_auto_1fr]">
        <div className="grid place-items-center h-8 col-start-2">
          <div>
            <a
              rel="noreferrer"
              className={
                "hover:underline hover:decoration-2 hover:decoration-orange-500"
              }
              href={graphiqlUrl}
              target={"_blank"}
            >
              Open Publy GraphiQL Explorer
            </a>{" "}
            |{" By "}
            <a
              rel="noreferrer"
              className={
                "hover:underline hover:decoration-2 hover:decoration-orange-500"
              }
              href={"https://nilshartmann.net"}
              target={"_blank"}
            >
              Nils Hartmann
            </a>
          </div>
        </div>
      </div>
    </footer>
  );
}
