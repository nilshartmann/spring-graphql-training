import React from "react";
import PageLayout from "../components/PageLayout";
import Stack from "../components/Stack";
import Input from "../components/Input";
import ButtonBar from "../components/ButtonBar";
import Button from "../components/Button";
import { useLocation, useNavigate } from "react-router-dom";
import { useAuthContext } from "../shared/AuthContext";
import { userServiceUrl } from "../urls";

type LoginStatus = {
  error?: string;
  running?: boolean;
};

const showLoginHint = process.env.REACT_APP_SHOW_LOGIN_HINT;

export default function LoginPage() {
  const auth = useAuthContext();
  const navigate = useNavigate();
  const { state } = useLocation();
  const [loginStatus, setLoginStatus] = React.useState<LoginStatus>();
  const [username, setUsername] = React.useState("");
  const [password, setPassword] = React.useState("");
  const [usernameTouched, setUsernameTouched] = React.useState(false);
  const [passwordTouched, setPasswordTouched] = React.useState(false);

  function handleUsernameChange(e: React.ChangeEvent<HTMLInputElement>) {
    setUsername(e.target.value);
    setUsernameTouched(true);
  }

  function handlePasswordChange(e: React.ChangeEvent<HTMLInputElement>) {
    setPassword(e.target.value);
    setPasswordTouched(true);
  }

  async function submitLogin() {
    setLoginStatus({ running: true });
    const response = await fetch(`${userServiceUrl}/login`, {
      method: "POST",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({ username, password }),
    });
    if (response.ok) {
      const payload = await response.json();
      console.log("PAYLOAD", payload);
      const { token } = payload;
      auth.setToken(token);
      if (state?.redirectTo) {
        navigate(state?.redirectTo, {
          replace: true,
        });
      } else {
        navigate("/");
      }
    } else {
      setLoginStatus({
        error: "Login failed",
      });
    }
  }

  return (
    <PageLayout>
      <Stack space={10}>
        {showLoginHint && <LoginHint />}

        <div
          className={"bg-orange-50 rounded-md border border-orange-100 p-4 "}
        >
          <Stack>
            <h2 className={"font-bold font-metro text-2xl"}>Login</h2>
            <Input
              label="Username"
              name="username"
              error={usernameTouched && !username && "Please fill in username"}
              value={username}
              onChange={handleUsernameChange}
            />
            <Input
              label="Password"
              name="password"
              type="password"
              error={passwordTouched && !password && "Please fill in password"}
              value={password}
              onChange={handlePasswordChange}
            />
            <p className={"text-orange-500 font-bold"}>
              {loginStatus?.error ? "Login failed!" : " "}
            </p>
            <ButtonBar>
              <Button onClick={submitLogin} disabled={loginStatus?.running}>
                Login
              </Button>
            </ButtonBar>
          </Stack>
        </div>
      </Stack>
    </PageLayout>
  );
}

function LoginHint() {
  return (
    <div className={"rounded-md border-2 border-orange-300 p-4 "}>
      <Stack>
        <p>
          To login, choose one of the following <b>Usernames</b>:
        </p>
        <Usernames />
        <p>
          And use any string as <b>password</b> that is longer than four chars
        </p>
      </Stack>
    </div>
  );
}

function Usernames() {
  const usernames = [
    "herman",
    "carroll",
    "willms",
    "murphy",
    "treutel",
    "purdy",
  ];

  return (
    <div className={"mt-1"}>
      <Stack orientation={"horizontal"}>
        {usernames.map((u) => (
          <UsernameBadge key={u}>{u}</UsernameBadge>
        ))}
      </Stack>
    </div>
  );
}

type UsernameBadgeProps = {
  children: React.ReactNode;
};

function UsernameBadge({ children }: UsernameBadgeProps) {
  return (
    <div className={"border-2 border-orange-300 p-1 rounded-md "}>
      {children}
    </div>
  );
}
