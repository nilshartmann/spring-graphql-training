import Stack from "./Stack";
import * as React from "react";
import { Link as RouterLink } from "react-router-dom";

type TagLineProps = {
  tags?: string[];
  activeTag?: string;
};
export default function TagLine({ tags, activeTag }: TagLineProps) {
  if (!tags) {
    return (
      <div className={"mt-1  font-light"}>
        <Stack orientation={"horizontal"}>
          <Tag>#tutorial</Tag>
          <Tag>#beginners</Tag>
          <Tag>#webdev</Tag>
          <Tag>#programming</Tag>
        </Stack>
      </div>
    );
  }
  return (
    <div className={"mt-1  font-light"}>
      <Stack orientation={"horizontal"}>
        {tags.map((tag) => (
          <Tag to={`/t/${tag}`} key={tag} active={tag === activeTag}>
            #{tag}
          </Tag>
        ))}
      </Stack>
    </div>
  );
}

type TagProps = {
  children: React.ReactNode;
  to?: string;
  active?: boolean;
};

function Tag({ children, to, active = false }: TagProps) {
  if (!to) {
    return <div className={"p-1"}>{children}</div>;
  }

  const className = active
    ? "border-2 border-orange-500 bg-orange-100 hover:border-2 hover:text-orange-500 hover:border-orange-500 hover:bg-orange-100 p-1 rounded-md "
    : "border-2 border-orange-50 hover:border-2 hover:text-orange-500 hover:border-orange-500 hover:bg-orange-100 p-1 rounded-md ";

  return (
    <div className={className}>
      <RouterLink to={to}>{children}</RouterLink>
    </div>
  );
}
