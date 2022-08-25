import { Link as RouterLink } from "react-router-dom";
import * as React from "react";

type CardProps = {
  children: React.ReactNode;
};
export default function Card({ children }: CardProps) {
  return (
    <section className={"bg-rose-50 border-rose-200 border rounded-md"}>
      {children}
    </section>
  );
}

type CardHeaderProps = {
  to?: string;
  title: string | React.ReactElement;
};

export function CardHeader({ to, title }: CardHeaderProps) {
  return to ? (
    <h2 className={"font-bold tracking-wide p-4 hover:text-rose-500"}>
      <RouterLink to={to}>{title}</RouterLink>
    </h2>
  ) : (
    <h2 className={"font-bold  tracking-wide p-4"}>{title}</h2>
  );
}

type CardItemProps = {
  children: React.ReactNode;
};

export function CardItem({ children }: CardItemProps) {
  return <div className={"border-t border-rose-200 p-4"}>{children}</div>;
}

type CardLinkProps = {
  to: string;
  title: string;
  subtitle: string;
};

export function CardLink({ to, title, subtitle }: CardLinkProps) {
  return (
    <div className={"group border-t border-rose-200 p-4 hover:bg-rose-100"}>
      <RouterLink to={to}>
        <div className={" group-hover:text-rose-500"}>{title}</div>
        <div className={" font-extralight text-sm"}>{subtitle}</div>
      </RouterLink>
    </div>
  );
}
